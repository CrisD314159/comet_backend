package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

public record UpdateUserDTO(
        @NotNull UUID id,
        String biography,
        String country,
        @NotBlank String name,
        @NotBlank @URL String profilePicture
        ) {
}
