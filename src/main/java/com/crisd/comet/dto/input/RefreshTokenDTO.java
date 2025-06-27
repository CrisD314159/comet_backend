package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDTO(
        @NotBlank String refreshToken

) {

}
