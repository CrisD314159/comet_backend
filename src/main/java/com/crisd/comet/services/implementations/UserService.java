package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.*;
import com.crisd.comet.dto.output.GetUserFriendsDTO;
import com.crisd.comet.dto.output.GetUserOverviewDTO;
import com.crisd.comet.exceptionHandling.exceptions.EntityNotFoundException;
import com.crisd.comet.exceptionHandling.exceptions.ValidationException;
import com.crisd.comet.mappers.UserMapper;
import com.crisd.comet.model.Friendship;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.UserState;
import com.crisd.comet.repositories.FriendshipRepository;
import com.crisd.comet.repositories.UserRepository;
import com.crisd.comet.services.interfaces.IEmailService;
import com.crisd.comet.services.interfaces.IUserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;
    private final FriendshipRepository friendshipRepository;
    private final UserMapper userMapper;

    @Override
    public void SignUp(SignUpDTO signUpDTO) throws MessagingException, IOException {

        var verificationCode = String.valueOf(
                ThreadLocalRandom.current().nextInt(1000, 10000));

        if(userRepository.existsUserByEmail(signUpDTO.email())) throw new ValidationException("User already exists");

        User user = User.builder()
                .name(signUpDTO.name())
                .profilePicture(signUpDTO.profilePictureUrl())
                .email(signUpDTO.email())
                .country(signUpDTO.country())
                .biography(signUpDTO.biography())
                .createdAt(LocalDateTime.now())
                .isVerified(false)
                .verificationCode(verificationCode)
                .password(passwordEncoder.encode(signUpDTO.password()))
                .build();

        emailService.SendMail(
                new EmailDetailsDTO(
                        user.getEmail(),
                        user.getName(),
                        verificationCode,
                        "Here's your verification code",
                        ""),
                "templates/verify_account.html");

        userRepository.save(user);
    }

    @Override
    public void UpdateUser(UpdateUserDTO updateUserDTO) {
        User user = GetValidUser(updateUserDTO.id());

        user.setBiography(updateUserDTO.biography());
        user.setCountry(updateUserDTO.country());
        user.setName(updateUserDTO.name());
        user.setProfilePicture(updateUserDTO.profilePicture());

        userRepository.save(user);
    }

    @Override
    public void DeleteUser(UUID userId) {
        User user = GetValidUser(userId);

        user.setState(UserState.DELETED);

        userRepository.save(user);
    }

    @Override
    public GetUserOverviewDTO GetUserAccountOverview(UUID userId) {
        User user = GetValidUser(userId);
        return userMapper.toDTO(user);
    }

    @Override
    public GetUserFriendsDTO GetUserFriends(UUID userId) {
        User user = GetValidUser(userId);
        ArrayList<Friendship> friendships = friendshipRepository.findAllByRequesterOrRecipient(user, user);

        ArrayList<User> friends = new ArrayList<>();

        for(Friendship friendship : friendships){
            if(!friendship.getRecipient().getId().equals(userId)){
                friends.add(friendship.getRecipient());
            }else{
                friends.add(friendship.getRequester());
            }
        }

        return userMapper.toFriendsDTO(friends);
    }

    @Override
    public User GetValidUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(user.getState() == UserState.DELETED) throw new EntityNotFoundException("User not found");
        if(!user.isVerified()) throw new ValidationException("User is not verified");
        return user;
    }

    @Override
    public void VerifyAccount(VerifyAccountDTO verifyAccountDTO) {
        User user = userRepository.findUserByEmail(verifyAccountDTO.email());

        if(user == null) throw new EntityNotFoundException("User not found");

        if(user.getVerificationCode().equals(verifyAccountDTO.code())){
            user.setVerified(true);
            user.setVerificationCode(null);
            userRepository.save(user);
        }else{
            throw new ValidationException("Invalid email or verification code");
        }
    }
}
