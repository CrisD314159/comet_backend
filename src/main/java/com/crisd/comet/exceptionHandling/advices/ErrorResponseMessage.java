package com.crisd.comet.exceptionHandling.advices;

public record ErrorResponseMessage<T>(
        boolean success,
        T message
) {
}
