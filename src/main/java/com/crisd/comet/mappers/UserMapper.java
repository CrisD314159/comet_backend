package com.crisd.comet.mappers;

import com.crisd.comet.dto.output.GetUserFriendsDTO;
import com.crisd.comet.dto.output.GetUserOverviewDTO;
import com.crisd.comet.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    GetUserOverviewDTO toDTO(User user);

    GetUserFriendsDTO toFriendsDTO(ArrayList<User> users);
}
