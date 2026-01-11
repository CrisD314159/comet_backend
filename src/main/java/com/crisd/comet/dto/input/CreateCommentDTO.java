package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record CreateCommentDTO(
        @NotNull UUID post_id,
        @NotNull @Length(max = 200) String content
) {
}
