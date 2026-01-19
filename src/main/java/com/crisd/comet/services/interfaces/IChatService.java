package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.StreamUserDetails;
import io.getstream.chat.java.exceptions.StreamException;

import java.util.UUID;

public interface IChatService {
    String generateToken(UUID userId);
    void UpsertStreamUser(StreamUserDetails user) throws StreamException;
}
