package com.crisd.comet.mappers;

import com.crisd.comet.dto.output.GetFriendRequestDTO;
import com.crisd.comet.dto.output.GetFriendRequestsDTO;
import com.crisd.comet.model.FriendRequest;
import com.crisd.comet.model.Friendship;
import com.crisd.comet.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendshipMapper {

    @Mapping(source = "requester", target = "userId", qualifiedByName ="userId")
    GetFriendRequestDTO toGetFriendRequestDTO(FriendRequest friendRequest);

    GetFriendRequestsDTO toRequestsDTO(ArrayList<FriendRequest> requests);

    @Named("userId")
    default UUID toUserId(User user) {
        return user.getId();
    }
}
