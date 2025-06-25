package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.LoginDTO;
import com.crisd.comet.dto.input.LogoutDTO;
import com.crisd.comet.dto.output.TokenResponseDTO;

import java.util.UUID;

public interface IAuthInterface {

    public TokenResponseDTO Login(LoginDTO loginDTO);
    public void Logout(LogoutDTO logoutDTO);
    public TokenResponseDTO refreshToken(String refreshToken);
}
