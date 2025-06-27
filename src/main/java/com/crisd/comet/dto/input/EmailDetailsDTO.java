package com.crisd.comet.dto.input;

public record EmailDetailsDTO (
        String to,
        String name,
        String verificationCode,
        String subject
){
}
