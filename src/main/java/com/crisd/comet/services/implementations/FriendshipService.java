package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.AcceptRejectFriendRequestDTO;
import com.crisd.comet.dto.input.BlockFriendDTO;
import com.crisd.comet.dto.input.SendFriendRequestDTO;
import com.crisd.comet.dto.output.GetFriendRequestReceiverDTO;
import com.crisd.comet.dto.output.GetFriendRequestRequesterDTO;
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
    public void SendFriendRequest(UUID requester, SendFriendRequestDTO sendFriendRequestDTO) {
        User requesterUser = userService.GetValidUser(requester);

        User recipient = userService.GetValidUser(sendFriendRequestDTO.receiver());

        if(requesterUser.getId().equals(recipient.getId())) {
            throw new ValidationException("You can't send friend requests with the same recipient");
        }

        if(RequestAlreadyExists(requesterUser, recipient)) {
            throw new ValidationException("Friend request already exists");
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .requester(requesterUser)
                .recipient(recipient)
                .dateRequested(LocalDateTime.now())
                .state(FriendRequestState.PENDING)
                .message(sendFriendRequestDTO.message())
                .build();


        friendRequestRepository.save(friendRequest);
    }

    private boolean RequestAlreadyExists(User requester, User recipient) {
        ArrayList<FriendRequest> friendRequest = Optional.ofNullable(friendRequestRepository.findByRecipientAndRequester(recipient, requester))
                .or(() -> Optional.ofNullable(friendRequestRepository.findByRecipientAndRequester(requester, recipient)))
                .orElseThrow(() -> new EntityNotFoundException("Friendship not found"));

        for(FriendRequest request : friendRequest){
            if (request.getState() != FriendRequestState.REJECTED){
                return true;
            }
        }
        return false;
    }

    @Override
    public void AcceptFriendRequest(UUID recipient, AcceptRejectFriendRequestDTO acceptRejectFriendRequestDTO) {

        FriendRequest friendRequest = friendRequestRepository.findByIdAndState(
                acceptRejectFriendRequestDTO.friendRequestId(),FriendRequestState.PENDING);

        if(friendRequest == null) {
            throw new EntityNotFoundException("Friend request not found");
        }

        if (!friendRequest.getRecipient().getId().equals(recipient))
            throw new ValidationException("Friend request does not belong to the recipient");

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
    public void RejectFriendRequest(UUID recipient, AcceptRejectFriendRequestDTO acceptRejectFriendRequestDTO) {
        FriendRequest friendRequest = friendRequestRepository.findByIdAndState(
                acceptRejectFriendRequestDTO.friendRequestId(),FriendRequestState.PENDING);

        if(friendRequest == null) {
            throw new EntityNotFoundException("Friend request not found");
        }

        friendRequest.setState(FriendRequestState.REJECTED);

        friendRequestRepository.save(friendRequest);
    }

    @Override
    public GetFriendRequestsDTO<GetFriendRequestRequesterDTO> GetFriendRequests(UUID userId) {
        User user = userService.GetValidUser(userId);
        ArrayList<FriendRequest> friendRequests = friendRequestRepository
                .findAllByRecipientAndState(user, FriendRequestState.PENDING);

        ArrayList<GetFriendRequestRequesterDTO> friendRequestsDTO = friendshipMapper.toRequestsDTOFromRequester(friendRequests);

        return new GetFriendRequestsDTO<>(friendRequestsDTO);
    }

    @Override
    public GetFriendRequestsDTO<GetFriendRequestReceiverDTO> GetOutGoingFriendRequests(UUID userId) {
        User user = userService.GetValidUser(userId);
        ArrayList<FriendRequest> friendRequests = friendRequestRepository
                .findAllByRequesterAndState(user, FriendRequestState.PENDING);

        ArrayList<GetFriendRequestReceiverDTO> outgoingFriendRequests = friendshipMapper.toRequestsDTOFromRecipient(friendRequests);

        return new GetFriendRequestsDTO<>(outgoingFriendRequests);
    }

    @Override
    public void BlockFriend(UUID requester, BlockFriendDTO blockFriendDTO) {
        User requesterUser = userService.GetValidUser(requester);
        User recipient = userService.GetValidUser(blockFriendDTO.friendId());
        Friendship friendship = findFriendship(requesterUser, recipient);

        friendship.setBlocked(true);
        friendshipRepository.save(friendship);
    }

    @Override
    public void UnblockFriend(UUID requester, BlockFriendDTO blockFriendDTO) {
        User requesterUser = userService.GetValidUser(requester);
        User recipient = userService.GetValidUser(blockFriendDTO.friendId());
        Friendship friendship = findFriendship(requesterUser, recipient);

        friendship.setBlocked(false);
        friendshipRepository.save(friendship);
    }

    @Override
    public void DeleteFriend(UUID requester, BlockFriendDTO blockFriendDTO) {
        User requesterUser = userService.GetValidUser(requester);
        User recipient = userService.GetValidUser(blockFriendDTO.friendId());
        Friendship friendship = findFriendship(requesterUser, recipient);

        ArrayList<FriendRequest> friendRequest = friendRequestRepository.findByRecipientAndRequester(friendship.getRecipient(), friendship.getRequester());

        for(FriendRequest request: friendRequest){
            if(request.getState() != FriendRequestState.REJECTED){
                friendRequestRepository.delete(request);
            }
        }
        friendshipRepository.delete(friendship);
    }


    private Friendship findFriendship(User user1, User user2) {
        return Optional.ofNullable(friendshipRepository.findByRequesterAndRecipient(user1, user2))
                .or(() -> Optional.ofNullable(friendshipRepository.findByRequesterAndRecipient(user2, user1)))
                .orElseThrow(() -> new EntityNotFoundException("Friendship not found"));
    }
}
