package com.crisd.comet.dto.input;

import jakarta.mail.Multipart;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record CreatePostDTO(
       @NotNull UUID userId,
       @Null String text,
       @Null List<MultipartFile> media
) {

}
