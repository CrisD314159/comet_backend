package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.StreamUserDetails;
import com.crisd.comet.dto.output.GetUserOverviewDTO;
import com.crisd.comet.model.User;
import io.getstream.chat.java.exceptions.StreamException;

import java.util.UUID;

public interface IChatService {
    String generateToken(UUID userId);
    void UpsertStreamUser(StreamUserDetails user) throws StreamException;

}
