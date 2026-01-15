package com.crisd.comet.services.implementations;

import com.crisd.comet.exceptionHandling.exceptions.EntityNotFoundException;
import com.crisd.comet.model.Post;
import com.crisd.comet.model.Reaction;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.PostState;
import com.crisd.comet.repositories.PostRepository;
import com.crisd.comet.repositories.ReactionRepository;
import com.crisd.comet.services.interfaces.IPostService;
import com.crisd.comet.services.interfaces.IReactionService;
import com.crisd.comet.services.interfaces.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class ReactionService implements IReactionService {

    private final IPostService postService;
    private final PostRepository postRepository;
    private final IUserService userService;
    private final ReactionRepository reactionRepository;

    @Override
    public void ReactToPost(UUID postId, UUID userReactsId) {
        Post post = postService.GetPostByStateAndId(postId, PostState.ACTIVE);
        User user = userService.GetValidUser(userReactsId);

        Reaction reaction = Reaction.builder().post(post).user(user).build();
        post.getReactions().add(reaction);

        postRepository.save(post);
    }

    @Override
    public void RemoveReactToPost(UUID postId, UUID userReactsId) {
        Post post = postService.GetPostByStateAndId(postId, PostState.ACTIVE);
        userService.GetValidUser(userReactsId);

        Optional<Reaction> reactionOptional =
                reactionRepository.findReactionByUser_IdAndPost_Id(userReactsId, postId);

        Reaction reaction = reactionOptional.orElseThrow(()->
                new EntityNotFoundException("Reaction not found"));

        post.getReactions().remove(reaction);

        postRepository.save(post);
    }
}
