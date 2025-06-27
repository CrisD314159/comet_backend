package com.crisd.comet.mappers;

import com.crisd.comet.dto.output.GetFriendRequestReceiverDTO;
import com.crisd.comet.dto.output.GetFriendRequestRequesterDTO;
import com.crisd.comet.model.FriendRequest;
import com.crisd.comet.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FriendshipMapper {

    @Mapping(source = "recipient", target = "userId", qualifiedByName ="userId")
    @Mapping(source = "recipient", target = "profilePicture", qualifiedByName ="profilePicture")
    @Mapping(source = "recipient", target = "biography", qualifiedByName ="biography")
    @Mapping(source = "recipient", target = "country", qualifiedByName ="country")
    @Mapping(source = "recipient", target = "name", qualifiedByName ="name")
    GetFriendRequestReceiverDTO toDTOFromRecipient(FriendRequest friendRequest);

    @Mapping(source = "requester", target = "userId", qualifiedByName ="userId")
    @Mapping(source = "requester", target = "profilePicture", qualifiedByName ="profilePicture")
    @Mapping(source = "requester", target = "biography", qualifiedByName ="biography")
    @Mapping(source = "requester", target = "country", qualifiedByName ="country")
    @Mapping(source = "requester", target = "name", qualifiedByName ="name")
    GetFriendRequestRequesterDTO toDTOFromRequester(FriendRequest friendRequest);

    ArrayList<GetFriendRequestReceiverDTO> toRequestsDTOFromRecipient(ArrayList<FriendRequest> requests);
    ArrayList<GetFriendRequestRequesterDTO> toRequestsDTOFromRequester(ArrayList<FriendRequest> requests);

    @Named("userId")
    default UUID toUserId(User user) {
        return user.getId();
    }

    @Named("profilePicture")
    default String toProfilePicture(User user) {
        return user.getProfilePicture();
    }

    @Named("biography")
    default String toBiography(User user) {
        return user.getBiography();
    }

    @Named("country")
    default String toCountry(User user) {
        return user.getCountry();
    }

    @Named("name")
    default String toName(User user) {
        return user.getName();
    }
}
