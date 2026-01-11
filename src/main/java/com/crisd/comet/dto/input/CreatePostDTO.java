package com.crisd.comet.dto.input;

import jakarta.mail.Multipart;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.UUID;

public record CreatePostDTO(
       @NotNull UUID userId,
       @Null String text,
       @Null Multipart media
) {

}
