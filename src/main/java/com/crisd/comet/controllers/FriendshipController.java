package com.crisd.comet.controllers;

import com.crisd.comet.dto.input.AcceptRejectFriendRequestDTO;
import com.crisd.comet.dto.input.BlockFriendDTO;
import com.crisd.comet.dto.input.SendFriendRequestDTO;
import com.crisd.comet.dto.output.EntityResponseMessage;
import com.crisd.comet.dto.output.GetFriendRequestReceiverDTO;
import com.crisd.comet.dto.output.GetFriendRequestRequesterDTO;
import com.crisd.comet.dto.output.GetFriendRequestsDTO;
import com.crisd.comet.security.UserDetailsImpl;
import com.crisd.comet.services.implementations.FriendshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;


    @PostMapping("/sendFriendRequest")
    public ResponseEntity<EntityResponseMessage> SendFriendRequest(
            @Valid @RequestBody SendFriendRequestDTO friendRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        UUID requesterId = userDetails.getId();
        friendshipService.SendFriendRequest(requesterId, friendRequestDTO);
        return ResponseEntity.ok(new EntityResponseMessage(true, "Friend request sent."));
    }

    @PutMapping("/acceptFriendRequest")
    public ResponseEntity<EntityResponseMessage> AcceptFriendRequest(
            @Valid @RequestBody AcceptRejectFriendRequestDTO friendRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID recipientId = userDetails.getId();
        friendshipService.AcceptFriendRequest(recipientId, friendRequestDTO);
        return ResponseEntity.ok(new EntityResponseMessage(true, "Friend request accepted."));
    }

    @PutMapping("/rejectFriendRequest")
    public ResponseEntity<EntityResponseMessage> RejectFriendRequest(
            @Valid @RequestBody AcceptRejectFriendRequestDTO friendRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID recipientId = userDetails.getId();
        friendshipService.RejectFriendRequest(recipientId, friendRequestDTO);
        return ResponseEntity.ok(new EntityResponseMessage(true, "Friend request rejected."));
    }

    @GetMapping("/getFriendRequests")
    public ResponseEntity<GetFriendRequestsDTO<GetFriendRequestRequesterDTO>> GetFriendRequests(@AuthenticationPrincipal UserDetailsImpl userDetails){
        UUID requesterId = userDetails.getId();
        return ResponseEntity.ok(friendshipService.GetFriendRequests(requesterId));
    }

    @GetMapping("/getOutgoingRequests")
    public ResponseEntity<GetFriendRequestsDTO<GetFriendRequestReceiverDTO>> GetOutgoingRequests(@AuthenticationPrincipal UserDetailsImpl userDetails){
        UUID requesterId = userDetails.getId();
        return ResponseEntity.ok(friendshipService.GetOutGoingFriendRequests(requesterId));
    }

    @PutMapping("/blockFriend")
    public ResponseEntity<EntityResponseMessage> BlockFriend(
            @Valid @RequestBody BlockFriendDTO friendDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        UUID requesterId = userDetails.getId();
        friendshipService.BlockFriend(requesterId, friendDTO);
        return ResponseEntity.ok(new EntityResponseMessage(true, "Friend blocked"));
    }

}
