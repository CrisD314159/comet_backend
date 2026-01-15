package com.crisd.comet.repositories;

import com.crisd.comet.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReactionRepository extends JpaRepository<Reaction, UUID> {
    Optional<Reaction> findReactionByUser_IdAndPost_Id(UUID userId, UUID postId);
}
