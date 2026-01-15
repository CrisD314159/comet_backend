package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RemoveImageDTO(
       @NotNull UUID postId,
       @NotNull String imageId
) {
}
