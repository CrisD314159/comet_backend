package com.crisd.comet.dto.output;

import java.util.UUID;

public record GetFriendRequestDTO(
        UUID id,
        UUID userId,
        String profilePicture,
        String biography,
        String country
) {

}
