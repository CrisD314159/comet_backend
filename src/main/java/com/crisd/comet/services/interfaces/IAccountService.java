package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.ChangePasswordDTO;
import com.crisd.comet.dto.input.LoginDTO;
import com.crisd.comet.dto.input.RefreshTokenDTO;
import com.crisd.comet.dto.input.RequestPasswordChangeDTO;
import com.crisd.comet.dto.output.TokenResponseDTO;
import com.crisd.comet.model.User;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface IAccountService {

     TokenResponseDTO Login(LoginDTO loginDTO);
     void Logout(RefreshTokenDTO refreshTokenDTO);
     TokenResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO);
     void ResetAccountEmail(RequestPasswordChangeDTO requestPasswordChangeDTO) throws MessagingException, IOException;
     void ChangePassword(ChangePasswordDTO changePasswordDTO);
     String CreateSession(User user);
}
