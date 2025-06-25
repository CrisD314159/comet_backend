package com.crisd.comet.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDTO(
       @NotBlank @Email String email,
       @NotBlank  String code,
       @NotBlank String password
) {


}
