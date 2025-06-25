package com.crisd.comet.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestPasswordChangeDTO (
        @NotBlank @Email String email
){

}
