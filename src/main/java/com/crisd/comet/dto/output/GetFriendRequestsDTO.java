package com.crisd.comet.dto.output;

import java.util.ArrayList;

public record GetFriendRequestsDTO (
        ArrayList<GetFriendRequestDTO> friendRequest
){
}
