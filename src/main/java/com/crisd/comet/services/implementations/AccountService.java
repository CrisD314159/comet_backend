package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.*;
import com.crisd.comet.dto.output.TokenResponseDTO;
import com.crisd.comet.exceptionHandling.exceptions.EntityNotFoundException;
import com.crisd.comet.exceptionHandling.exceptions.UnauthorizedException;
import com.crisd.comet.exceptionHandling.exceptions.ValidationException;
import com.crisd.comet.model.Session;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.UserState;
import com.crisd.comet.repositories.SessionRepository;
import com.crisd.comet.repositories.UserRepository;
import com.crisd.comet.security.JWTUtil;
import com.crisd.comet.services.interfaces.IAccountService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements IAccountService {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    @Value("${spring.front}")
    private String front;

    @Override
    public TokenResponseDTO Login(LoginDTO loginDTO) {
        User userEmail = userService.GetValidByEmail(loginDTO.email());
        if(userEmail.isCreatedWithGoogle()) throw  new ValidationException("Use your google account to sign in");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
        );

        if (authentication.isAuthenticated()) {
            User user = userRepository.findUserByEmail(authentication.getName());
            String token = jwtUtil.GenerateToken(user.getId(), user.getEmail(), false, null);
            String refreshToken = CreateSession(user);
            return new TokenResponseDTO(token, refreshToken);
        }

        throw new ValidationException("Invalid email or password");
    }



    @Override
    public void Logout(RefreshTokenDTO refreshTokenDTO) {
        UUID sessionId = jwtUtil.GetSessionIdFromRefreshToken(refreshTokenDTO.refreshToken());
        Session session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new UnauthorizedException("Session not found")
        );
        sessionRepository.delete(session);
    }

    @Override
    @Transactional(dontRollbackOn = UnauthorizedException.class)
    public TokenResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        UUID sessionId = jwtUtil.GetSessionIdFromRefreshToken(refreshTokenDTO.refreshToken());
        if(sessionId == null) {
            throw new ValidationException("Invalid refresh token");
        }

        Session session = sessionRepository.findById(sessionId).orElseThrow(
                ()-> new EntityNotFoundException("Session not found")
        );

        if(session.getExpiresAt().isBefore(LocalDateTime.now())) {
            sessionRepository.delete(session);
            throw new UnauthorizedException("Session expired");
        }

        User sessionUser = session.getUser();
        if (Duration.between(LocalDateTime.now(), session.getExpiresAt()).toDays() <= 2) {
            session.setExpiresAt(LocalDateTime.now().plusDays(7));
            sessionRepository.save(session);
            String token = jwtUtil.GenerateToken(sessionUser.getId(), sessionUser.getEmail(), false, null);
            String refreshToken = jwtUtil.GenerateToken(sessionUser.getId(), sessionUser.getEmail(),
                    true, session.getId().toString());

            return new TokenResponseDTO(token, refreshToken);
        }else{
            String token = jwtUtil.GenerateToken(sessionUser.getId(), sessionUser.getEmail(), false, null);
            return new TokenResponseDTO(token, null);
        }
    }


    @Override
    public void ResetAccountEmail(RequestPasswordChangeDTO requestPasswordChangeDTO) throws MessagingException, IOException {
        User user = userService.GetValidByEmail(requestPasswordChangeDTO.email());
        if(user == null) throw new EntityNotFoundException("User not found");
        if(user.isCreatedWithGoogle()) throw  new ValidationException("Use your google account to sign in");

        String token = jwtUtil.GenerateToken(user.getId(), user.getEmail(), false, null);

        user.setVerificationCode(token);

        String tokenEncoded = URLEncoder.encode(token, StandardCharsets.UTF_8);

        String url = String.format("%s/resetAccount?code={%s}", front, tokenEncoded);

        emailService.SendMail(new EmailDetailsDTO(
                user.getEmail(), user.getName(), url, "Here's your recovery code for Comet"),
                "templates/recover_account.html");
        userRepository.save(user);

    }

    @Override
    public void ChangePassword(ChangePasswordDTO changePasswordDTO) {
        User user = userService.GetValidByEmail(changePasswordDTO.email());
        if(user == null) throw new EntityNotFoundException("User not found");
        if(user.isCreatedWithGoogle()) throw  new ValidationException("Use your google account to sign in");

        if(!IsValidRecoveryToken(changePasswordDTO.code(), user.getVerificationCode(), user.getEmail()))
            throw new ValidationException("Invalid email or verification code");

        user.setPassword(passwordEncoder.encode(changePasswordDTO.password()));
        user.setVerificationCode(null);
        userRepository.save(user);
    }

    private boolean IsValidRecoveryToken(String providedToken, String userToken, String userEmail){
        return jwtUtil.ValidateRecoverToken(providedToken, userEmail) && providedToken.equals(userToken);
    }

    @Override
    public String CreateSession(User user){
        Session session = Session.builder()
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .user(user)
                .build();

        sessionRepository.saveAndFlush(session);
        return jwtUtil.GenerateToken(user.getId(), user.getEmail(), true, session.getId().toString());

    }
}
