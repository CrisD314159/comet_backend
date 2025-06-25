package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.AcceptRejectFriendRequestDTO;
import com.crisd.comet.dto.input.BlockFriendDTO;
import com.crisd.comet.dto.input.SendFriendRequestDTO;
import com.crisd.comet.dto.output.GetFriendRequestsDTO;

import java.util.UUID;

public interface IFriendshipService {
    public void SendFriendRequest(SendFriendRequestDTO sendFriendRequestDTO);
    public void AcceptFriendRequest(AcceptRejectFriendRequestDTO acceptRejectFriendRequestDTO);
    public void RejectFriendRequest(AcceptRejectFriendRequestDTO acceptRejectFriendRequestDTO);
    public GetFriendRequestsDTO GetFriendRequests(UUID userId);
    public GetFriendRequestsDTO GetOutGoingFriendRequests(UUID userId);
    public void BlockFriend(BlockFriendDTO blockFriendDTO);
}
