package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AcceptRejectFriendRequestDTO(
      @NotNull UUID friendRequestId
) {

}
