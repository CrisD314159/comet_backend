package com.crisd.comet.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO (
        @NotBlank(message = "Email must not be blank") @Email String email,
        @NotBlank(message = "Password must not be blank") String password
) {
}
