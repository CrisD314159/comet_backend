package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ArchiveChatDTO(
        @NotNull UUID post_id
) {
}
