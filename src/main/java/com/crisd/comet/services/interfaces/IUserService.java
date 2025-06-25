package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.*;
import com.crisd.comet.dto.output.GetUserFriendsDTO;
import com.crisd.comet.dto.output.GetUserOverviewDTO;
import com.crisd.comet.model.User;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.UUID;

public interface IUserService {

    public void SignUp(SignUpDTO signUpDTO) throws MessagingException, IOException;
    public void UpdateUser(UpdateUserDTO updateUserDTO);
    public void DeleteUser(UUID userId);
    public GetUserOverviewDTO GetUserAccountOverview(UUID userId);
    public GetUserFriendsDTO GetUserFriends(UUID userId);
    public User GetValidUser(UUID uuid);
    public void VerifyAccount(VerifyAccountDTO verifyAccountDTO);


}
