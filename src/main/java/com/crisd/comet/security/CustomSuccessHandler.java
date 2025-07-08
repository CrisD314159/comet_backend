package com.crisd.comet.security;

import com.crisd.comet.dto.output.TokenResponseDTO;
import com.crisd.comet.services.interfaces.IAccountService;
import com.crisd.comet.services.interfaces.IThirdPartyAccountService;
import io.getstream.chat.java.exceptions.StreamException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * In this class we define the personalized steps when an oauth2 login was successful
     * onAuthenticationSuccess allows us o retrieve user data to handle it the way we want it
     * in this case que retrieve the user data to register the user en return a jwt to the frontend application
     */

    private final IThirdPartyAccountService accountService;

    @Value("${spring.front}")
    private String frontEndpoint;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = oauthToken.getPrincipal();
        String name = principal.getAttribute("name");
        String email = principal.getAttribute("email");
        String profilePicture = principal.getAttribute("picture");

        TokenResponseDTO tokens = null;
        try {
            tokens = accountService.LoginWithThirdParty(email, name, profilePicture);
        } catch (StreamException e) {
            throw new RuntimeException(e);
        }

        String url = String.format("%s/api/auth/google/callback?token=%s&refresh=%s", frontEndpoint, tokens.token(), tokens.refreshToken());
        response.sendRedirect(url);
    }
}
