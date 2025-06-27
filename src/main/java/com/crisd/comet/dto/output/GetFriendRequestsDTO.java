package com.crisd.comet.dto.output;

import java.util.ArrayList;

public record GetFriendRequestsDTO<T> (
        ArrayList<T> friendRequest
){
}
