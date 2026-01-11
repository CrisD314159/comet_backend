package com.crisd.comet.dto.output;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetPostCommentDTO(
        UUID id,
        UUID authorId,
        String authorName,
        UUID postId,
        LocalDateTime datePublished
) {
}
