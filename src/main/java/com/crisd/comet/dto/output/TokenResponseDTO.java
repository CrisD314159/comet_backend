package com.crisd.comet.dto.output;

public record TokenResponseDTO(
        String token,
        String refreshToken
) {
}
