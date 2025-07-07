package com.crisd.comet.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestPasswordChangeDTO (
        @NotBlank(message = "Email mus not be blank") @Email String email
){

}
