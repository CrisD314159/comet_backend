package com.crisd.comet.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record SignUpDTO(
        @NotBlank @Length(max = 60) String name,
       @Length(max = 200)  String biography,
       @Length(max = 60) String country,
       @NotBlank @Email String email,
        @NotBlank @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
                message = "The password must be at least 8 characters, one letter, one number and a special character"
        ) String password,
        @NotBlank @URL String profilePictureUrl

) {
}
