package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.AcceptRejectFriendRequestDTO;
import com.crisd.comet.dto.input.BlockFriendDTO;
import com.crisd.comet.dto.input.SendFriendRequestDTO;
import com.crisd.comet.dto.output.GetFriendRequestsDTO;
import com.crisd.comet.exceptionHandling.exceptions.EntityNotFoundException;
import com.crisd.comet.exceptionHandling.exceptions.ValidationException;
import com.crisd.comet.mappers.FriendshipMapper;
import com.crisd.comet.model.FriendRequest;
import com.crisd.comet.model.Friendship;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.FriendRequestState;
import com.crisd.comet.repositories.FriendRequestRepository;
import com.crisd.comet.repositories.FriendshipRepository;
import com.crisd.comet.services.interfaces.IFriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendshipService implements IFriendshipService {

    private final UserService userService;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipMapper friendshipMapper;
    @Override
    public void SendFriendRequest(SendFriendRequestDTO sendFriendRequestDTO) {
        User requester = userService.GetValidUser(sendFriendRequestDTO.requester());

        User recipient = userService.GetValidUser(sendFriendRequestDTO.receiver());

        if(friendRequestRepository.existsFriendRequestByRecipientOrRequester(requester, recipient)) {
            throw new ValidationException("Friend request already exists");
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .requester(requester)
                .recipient(recipient)
                .dateRequested(LocalDateTime.now())
                .state(FriendRequestState.PENDING)
                .message(sendFriendRequestDTO.message())
                .build();


        friendRequestRepository.save(friendRequest);
    }

    @Override
    public void AcceptFriendRequest(AcceptRejectFriendRequestDTO acceptRejectFriendRequestDTO) {

        FriendRequest friendRequest = friendRequestRepository.findByIdAndState(
                acceptRejectFriendRequestDTO.friendRequestId(),FriendRequestState.PENDING);

        if(friendRequest == null) {
            throw new EntityNotFoundException("Friend request not found");
        }

        friendRequest.setState(FriendRequestState.ACCEPTED);

        Friendship friendship = Friendship.builder()
                .requester(friendRequest.getRequester())
                .recipient(friendRequest.getRecipient())
                .dateAccepted(LocalDateTime.now())
                .isBlocked(false)
                .build();
        friendRequestRepository.save(friendRequest);
        friendshipRepository.save(friendship);
    }

    @Override
    public void RejectFriendRequest(AcceptRejectFriendRequestDTO acceptRejectFriendRequestDTO) {
        FriendRequest friendRequest = friendRequestRepository.findByIdAndState(
                acceptRejectFriendRequestDTO.friendRequestId(),FriendRequestState.PENDING);

        if(friendRequest == null) {
            throw new EntityNotFoundException("Friend request not found");
        }
        friendRequest.setState(FriendRequestState.REJECTED);

        friendRequestRepository.save(friendRequest);
    }

    @Override
    public GetFriendRequestsDTO GetFriendRequests(UUID userId) {
        User user = userService.GetValidUser(userId);
        ArrayList<FriendRequest> friendRequests = friendRequestRepository
                .findAllByRecipientAndState(user, FriendRequestState.PENDING);

        return friendshipMapper.toRequestsDTO(friendRequests);
    }

    @Override
    public GetFriendRequestsDTO GetOutGoingFriendRequests(UUID userId) {
        User user = userService.GetValidUser(userId);
        ArrayList<FriendRequest> friendRequests = friendRequestRepository
                .findAllByRequesterAndState(user, FriendRequestState.PENDING);

        return friendshipMapper.toRequestsDTO(friendRequests);
    }

    @Override
    public void BlockFriend(BlockFriendDTO blockFriendDTO) {
        User requester = userService.GetValidUser(blockFriendDTO.requester());
        User recipient = userService.GetValidUser(blockFriendDTO.friendId());
        Friendship friendship = findFriendship(requester, recipient);

        friendship.setBlocked(true);
        friendshipRepository.save(friendship);
    }

    private Friendship findFriendship(User user1, User user2) {
        return Optional.ofNullable(friendshipRepository.findByRequesterAndRecipient(user1, user2))
                .or(() -> Optional.ofNullable(friendshipRepository.findByRequesterAndRecipient(user2, user1)))
                .orElseThrow(() -> new EntityNotFoundException("Friendship not found"));
    }
}
