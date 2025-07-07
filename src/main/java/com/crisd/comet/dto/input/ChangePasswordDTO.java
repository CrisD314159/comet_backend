package com.crisd.comet.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordDTO(
       @NotBlank (message = "Email must not be blank") @Email String email,
       @NotBlank(message = "Code must not be blank")  String code,
       @NotBlank @Pattern(
               regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
               message = "The password must be at least 8 characters, one letter, one number and a special character"
       )  String password
) {


}
