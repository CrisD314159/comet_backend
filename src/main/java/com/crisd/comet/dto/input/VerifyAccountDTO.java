package com.crisd.comet.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyAccountDTO(
        @NotBlank(message = "Email must not be blank") @Email String email,
        @NotBlank(message = "Code must not be blank") String code
) {
}
