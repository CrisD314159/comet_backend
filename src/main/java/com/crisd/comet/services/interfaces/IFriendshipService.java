package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.AcceptRejectFriendRequestDTO;
import com.crisd.comet.dto.input.BlockFriendDTO;
import com.crisd.comet.dto.input.SendFriendRequestDTO;
import com.crisd.comet.dto.output.GetFriendRequestReceiverDTO;
import com.crisd.comet.dto.output.GetFriendRequestRequesterDTO;
import com.crisd.comet.dto.output.GetFriendRequestsDTO;
import com.crisd.comet.model.Friendship;
import com.crisd.comet.model.User;

import java.util.UUID;

public interface IFriendshipService {
     void SendFriendRequest(UUID requester, SendFriendRequestDTO sendFriendRequestDTO);
     void AcceptFriendRequest(UUID recipient, AcceptRejectFriendRequestDTO acceptRejectFriendRequestDTO);
     void RejectFriendRequest(UUID recipient, AcceptRejectFriendRequestDTO acceptRejectFriendRequestDTO);
     GetFriendRequestsDTO<GetFriendRequestRequesterDTO> GetFriendRequests(UUID userId);
     GetFriendRequestsDTO<GetFriendRequestReceiverDTO> GetOutGoingFriendRequests(UUID userId);
     void BlockFriend(UUID requester, BlockFriendDTO blockFriendDTO);
     void UnblockFriend(UUID requester, BlockFriendDTO blockFriendDTO);
     void DeleteFriend(UUID requester, BlockFriendDTO blockFriendDTO);
}
