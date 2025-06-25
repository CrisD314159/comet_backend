package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.LoginDTO;
import com.crisd.comet.dto.input.LogoutDTO;
import com.crisd.comet.dto.output.TokenResponseDTO;
import com.crisd.comet.security.JWTUtil;
import com.crisd.comet.services.interfaces.IAuthInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements IAuthInterface {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponseDTO Login(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
        );

        if (authentication.isAuthenticated()) {

        }
        return new TokenResponseDTO("", "");
    }

    @Override
    public void Logout(LogoutDTO logoutDTO) {

    }

    @Override
    public TokenResponseDTO refreshToken(String refreshToken) {
        return null;
    }

    private String CreateSession(){
        return "";
    }
}
