package com.crisd.comet.dto.output;

import java.util.UUID;

public record GetUserOverviewDTO (
        UUID id,
        String name,
        String profilePicture,
        String biography,
        String country,
        boolean blocked,
        boolean createdWithGoogle
){
}
