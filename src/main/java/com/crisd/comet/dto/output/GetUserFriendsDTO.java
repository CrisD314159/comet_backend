package com.crisd.comet.dto.output;

import java.util.ArrayList;

public record GetUserFriendsDTO (
        ArrayList<GetUserOverviewDTO> userFriends
) {
}
