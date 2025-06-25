package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record BlockFriendDTO(
        @NotBlank UUID requester,
        @NotBlank UUID friendId
        ) {
}
