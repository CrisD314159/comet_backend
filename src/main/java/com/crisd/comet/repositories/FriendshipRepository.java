package com.crisd.comet.repositories;

import com.crisd.comet.model.Friendship;
import com.crisd.comet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {

    ArrayList<Friendship> findAllByRequesterOrRecipient(User requester, User recipient);
    Friendship findByRequesterAndRecipient(User requester, User recipient);
}
