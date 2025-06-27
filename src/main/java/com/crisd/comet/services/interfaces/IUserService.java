package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.*;
import com.crisd.comet.dto.output.GetUserFriendsDTO;
import com.crisd.comet.dto.output.GetUserOverviewDTO;
import com.crisd.comet.model.User;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.UUID;

public interface IUserService {

     void SignUp(SignUpDTO signUpDTO) throws MessagingException, IOException;
     void UpdateUser(UpdateUserDTO updateUserDTO);
     void DeleteUser(UUID userId);
     GetUserOverviewDTO GetUserAccountOverview(UUID userId);
     GetUserFriendsDTO GetUserFriends(UUID userId);
     User GetValidUser(UUID uuid);
     User GetValidByEmail(String email);
     void VerifyAccount(VerifyAccountDTO verifyAccountDTO);


}
