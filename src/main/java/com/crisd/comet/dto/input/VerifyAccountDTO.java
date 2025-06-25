package com.crisd.comet.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyAccountDTO(
        @NotBlank @Email String email,
        @NotBlank String code
) {
}
