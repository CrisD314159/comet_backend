package com.crisd.comet.dto.output;

import java.util.UUID;

public record GetUserOverviewDTO (
        UUID id,
        String profilePicture,
        String biography,
        String country
){
}
