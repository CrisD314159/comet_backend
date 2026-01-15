package com.crisd.comet.repositories;

import com.crisd.comet.model.Post;
import com.crisd.comet.model.enums.PostState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findPostByIdAndPostState(UUID id, PostState postState);
    Page<Post> findPostsByAuthor_IdAndPostState(UUID authorId, PostState postState, Pageable pageable);
    Page<Post> findPostsByPostState(PostState postState, Pageable pageable);
}
