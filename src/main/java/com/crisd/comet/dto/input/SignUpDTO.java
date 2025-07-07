package com.crisd.comet.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record SignUpDTO(
        @NotBlank(message = "Name must not be blank") @Length(max = 60) String name,
       @Length(max = 200)  String biography,
       @Length(max = 30) String country,
       @NotBlank (message = "Email must not be blank") @Email String email,
        @NotBlank @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
                message = "The password must be at least 8 characters, one letter, one number and a special character"
        ) String password,
        @NotBlank(message = "Profile picture must not be blank") @URL String profilePictureUrl

) {
}
