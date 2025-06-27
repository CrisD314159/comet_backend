package com.crisd.comet.controllers;

import com.crisd.comet.dto.input.ChangePasswordDTO;
import com.crisd.comet.dto.input.LoginDTO;
import com.crisd.comet.dto.input.RefreshTokenDTO;
import com.crisd.comet.dto.input.RequestPasswordChangeDTO;
import com.crisd.comet.dto.output.EntityResponseMessage;
import com.crisd.comet.dto.output.TokenResponseDTO;
import com.crisd.comet.services.implementations.AccountService;
import com.crisd.comet.services.implementations.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        TokenResponseDTO responseDTO = accountService.Login(loginDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        TokenResponseDTO responseDTO = accountService.refreshToken(refreshTokenDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<EntityResponseMessage> logout(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        accountService.Logout(refreshTokenDTO);
        return ResponseEntity.ok(new EntityResponseMessage(true, "User logged out successfully"));
    }

    @PostMapping("/resetAccount")
    public ResponseEntity<EntityResponseMessage> resetAccount(@Valid @RequestBody RequestPasswordChangeDTO passwordChangeDTO) throws MessagingException, IOException {
        accountService.ResetAccountEmail(passwordChangeDTO);
        return ResponseEntity.ok(new EntityResponseMessage(true, "A recovery code has been sent to your email"));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<EntityResponseMessage> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        accountService.ChangePassword(changePasswordDTO);
        return ResponseEntity.ok(new EntityResponseMessage(true, "Your password has been changed successfully"));
    }



}
