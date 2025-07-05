package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.output.TokenResponseDTO;
import com.crisd.comet.model.User;
import io.getstream.chat.java.exceptions.StreamException;

public interface IThirdPartyAccountService {
    TokenResponseDTO LoginWithThirdParty(String email, String name, String profilePicture) throws StreamException;
    User GetOrCreateValidUser(String email, String name, String profilePicture) throws StreamException;
}
