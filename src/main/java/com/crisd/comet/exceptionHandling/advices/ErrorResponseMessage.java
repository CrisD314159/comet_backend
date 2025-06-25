package com.crisd.comet.exceptionHandling.advices;

public record ErrorResponseMessage(
        int code,
        String message
) {
}
