package com.crisd.comet.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

public record UpdateUserDTO(
        @NotNull UUID id,
        @Length(max = 200) String biography,
        @Length(max = 30) String country,
        @NotBlank @Length(max = 60) String name,
        @NotBlank @URL String profilePicture
        ) {
}
