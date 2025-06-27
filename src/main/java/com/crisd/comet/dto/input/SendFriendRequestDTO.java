package com.crisd.comet.dto.input;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SendFriendRequestDTO(
        @NotNull UUID receiver,
        String message
        ) {
}
