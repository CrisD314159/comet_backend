package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

public record UpdateUserDTO(
        @NotBlank UUID id,
        String biography,
        String country,
        @NotBlank String name,
        @NotBlank @URL String profilePicture
        ) {
}
