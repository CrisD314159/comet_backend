package com.crisd.comet.repositories;

import com.crisd.comet.model.FriendRequest;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.FriendRequestState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {

    ArrayList<FriendRequest> findByRecipientAndRequester(User recipient, User requester);

    FriendRequest findByRecipientAndRequesterAndState(User recipient, User requester, FriendRequestState state);

    FriendRequest findByIdAndState(UUID id, FriendRequestState state);

    ArrayList<FriendRequest> findAllByRecipientAndState(User recipient, FriendRequestState state);

    ArrayList<FriendRequest> findAllByRequesterAndState(User requester, FriendRequestState state);

}
