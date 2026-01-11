package com.crisd.comet.dto.output;

import java.time.LocalDate;
import java.util.UUID;

public record GetPostDTO(
        UUID id,
        UUID authorId,
        String authorName,
        String description,
        String media,
        LocalDate datePosted,
        int reactionsCount
) {
}
