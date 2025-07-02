package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.StreamUserDetails;
import com.crisd.comet.dto.output.GetUserOverviewDTO;
import com.crisd.comet.services.interfaces.IChatService;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService implements IChatService {
    @Override
    public String generateToken(UUID userId) {
        String idToString = userId.toString();
        return User.createToken(idToString, null, null);
    }

    @Override
    public void UpsertStreamUser(StreamUserDetails user) throws StreamException {
        User.upsert()
                .user(User.UserRequestObject.builder()
                        .id(user.id())
                        .name(user.name())
                        .additionalField("profilePicture", user.profilePicture())
                        .build())
                .request();
    }
}
