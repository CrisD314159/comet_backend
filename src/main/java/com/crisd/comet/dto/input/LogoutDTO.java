package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotBlank;

public record LogoutDTO (
        @NotBlank String refreshToken

) {

}
