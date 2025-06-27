package com.crisd.comet.dto.output;

import java.util.UUID;

public record GetFriendRequestRequesterDTO (
        UUID id,
        UUID userId,
        String profilePicture,
        String name,
        String biography,
        String country
){
}
