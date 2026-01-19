package com.crisd.comet.services;

import com.crisd.comet.exceptionHandling.exceptions.EntityNotFoundException;
import com.crisd.comet.model.Post;
import com.crisd.comet.model.Reaction;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.PostState;
import com.crisd.comet.repositories.PostRepository;
import com.crisd.comet.repositories.ReactionRepository;
import com.crisd.comet.services.implementations.ReactionService;
import com.crisd.comet.services.interfaces.IPostService;
import com.crisd.comet.services.interfaces.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReactionServiceUnitTests {

    @Mock
    private IPostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private IUserService userService;

    @Mock
    private ReactionRepository reactionRepository;

    @InjectMocks
    private ReactionService reactionService;

    @Test
    void ReactToPost_ShouldAddReaction_WhenPostAndUserAreValid() {
        // Arrange
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        User user = User.builder()
                .id(userId)
                .build();
        
        Post post = Post.builder()
                .id(postId)
                .postState(PostState.ACTIVE)
                .reactions(new HashSet<>())
                .build();

        when(postService.GetPostByStateAndId(postId, PostState.ACTIVE)).thenReturn(post);
        when(userService.GetValidUser(userId)).thenReturn(user);

        // Act
        reactionService.ReactToPost(postId, userId);

        // Assert
        verify(postService).GetPostByStateAndId(postId, PostState.ACTIVE);
        verify(userService).GetValidUser(userId);
        verify(postRepository).save(post);
        
        assertEquals(1, post.getReactions().size());
        Reaction addedReaction = post.getReactions().iterator().next();
        assertEquals(user, addedReaction.getUser());
        assertEquals(post, addedReaction.getPost());
    }

    @Test
    void RemoveReactToPost_ShouldRemoveReaction_WhenReactionExists() {
        // Arrange
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        User user = User.builder()
                .id(userId)
                .build();
        
        Reaction reaction = Reaction.builder()
                .id(UUID.randomUUID())
                .user(user)
                .build();
        
        Post post = Post.builder()
                .id(postId)
                .postState(PostState.ACTIVE)
                .reactions(new HashSet<>())
                .build();
        
        post.getReactions().add(reaction);
        reaction.setPost(post);

        when(postService.GetPostByStateAndId(postId, PostState.ACTIVE)).thenReturn(post);
        when(userService.GetValidUser(userId)).thenReturn(user);
        when(reactionRepository.findReactionByUser_IdAndPost_Id(userId, postId)).thenReturn(Optional.of(reaction));

        // Act
        reactionService.RemoveReactToPost(postId, userId);

        // Assert
        verify(postService).GetPostByStateAndId(postId, PostState.ACTIVE);
        verify(userService).GetValidUser(userId);
        verify(reactionRepository).findReactionByUser_IdAndPost_Id(userId, postId);
        verify(postRepository).save(post);
        
        assertEquals(0, post.getReactions().size());
    }

    @Test
    void RemoveReactToPost_ShouldThrowException_WhenReactionNotFound() {
        // Arrange
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        User user = User.builder()
                .id(userId)
                .build();
        
        Post post = Post.builder()
                .id(postId)
                .postState(PostState.ACTIVE)
                .reactions(new HashSet<>())
                .build();

        when(postService.GetPostByStateAndId(postId, PostState.ACTIVE)).thenReturn(post);
        when(userService.GetValidUser(userId)).thenReturn(user);
        when(reactionRepository.findReactionByUser_IdAndPost_Id(userId, postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> reactionService.RemoveReactToPost(postId, userId));
        
        verify(postService).GetPostByStateAndId(postId, PostState.ACTIVE);
        verify(userService).GetValidUser(userId);
        verify(reactionRepository).findReactionByUser_IdAndPost_Id(userId, postId);
        verify(postRepository, never()).save(any(Post.class));
    }
}
