package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AcceptRejectFriendRequestDTO(
      @NotBlank UUID friendRequestId,
      @NotBlank  UUID receiverId
) {

}
