package com.crisd.comet.dto.output;

import com.crisd.comet.model.Image;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetPostDTO(
        UUID id,
        UUID authorId,
        String authorName,
        String description,
        List<Image> media,
        LocalDate datePosted,
        int reactionsCount
) {
}
