package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.StreamUserDetails;
import com.crisd.comet.dto.output.TokenResponseDTO;
import com.crisd.comet.model.Session;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.UserState;
import com.crisd.comet.repositories.SessionRepository;
import com.crisd.comet.repositories.UserRepository;
import com.crisd.comet.security.JWTUtil;
import com.crisd.comet.services.interfaces.IChatService;
import com.crisd.comet.services.interfaces.IThirdPartyAccountService;
import io.getstream.chat.java.exceptions.StreamException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ThirdPartyAccountService implements IThirdPartyAccountService {
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final IChatService chatService;


    @Override
    public TokenResponseDTO LoginWithThirdParty(String email, String name, String profilePicture) throws StreamException {
        User user = GetOrCreateValidUser(email, name, profilePicture);
        String token = jwtUtil.GenerateToken(user.getId(), user.getEmail(), false, null);
        String refreshToken = CreateSession(user);
        return new TokenResponseDTO(token, refreshToken);
    }

    @Override
    public User GetOrCreateValidUser(String email, String name, String profilePicture) throws StreamException {
        if(userRepository.existsUserByEmail(email)){
            return userRepository.findUserByEmail(email);
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .profilePicture(profilePicture)
                .isVerified(true)
                .verificationCode(null)
                .password("ThirdPartyUser")
                .country("Not Provided")
                .biography("Hello, i'm " + name )
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .state(UserState.ACTIVE)
                .createdWithGoogle(true)
                .build();

        userRepository.save(user);
        chatService.UpsertStreamUser(
                new StreamUserDetails(user.getId().toString(), user.getName(), user.getProfilePicture()));
        return user;
    }


    private String CreateSession(User user){
        Session session = Session.builder()
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .user(user)
                .build();

        sessionRepository.saveAndFlush(session);
        return jwtUtil.GenerateToken(user.getId(), user.getEmail(), true, session.getId().toString());

    }
}
