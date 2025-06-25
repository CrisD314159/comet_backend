package com.crisd.comet.dto.output;

public record EntityResponseMessage(
        boolean success,
        String message
) {
}
