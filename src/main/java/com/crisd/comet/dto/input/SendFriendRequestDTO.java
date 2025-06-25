package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SendFriendRequestDTO(
        @NotBlank UUID requester,
        @NotBlank UUID receiver,
        String message
        ) {
}
