package com.crisd.comet.services;

import com.crisd.comet.dto.input.CreateCommentDTO;
import com.crisd.comet.dto.output.GetPostCommentDTO;
import com.crisd.comet.mappers.CommentMapper;
import com.crisd.comet.model.Comment;
import com.crisd.comet.model.Post;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.PostState;
import com.crisd.comet.repositories.PostRepository;
import com.crisd.comet.services.implementations.CommentService;
import com.crisd.comet.services.interfaces.IPostService;
import com.crisd.comet.services.interfaces.IUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceUnitTests {

    @Mock
    private IPostService postService;

    @Mock
    private IUserService userService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @Test
    void getPostComments_ShouldReturnComments() {
        // Arrange
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        Comment comment = Comment.builder()
                .id(UUID.randomUUID())
                .datePublished(LocalDateTime.now())
                .build();
        
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        
        Post post = Post.builder()
                .id(postId)
                .comments(comments)
                .build();
        
        GetPostCommentDTO dto = new GetPostCommentDTO(
                comment.getId(),
                UUID.randomUUID(),
                "Author Name",
                postId,
                comment.getDatePublished()
        );
        List<GetPostCommentDTO> expectedDtos = List.of(dto);

        when(postService.GetPostByStateAndId(postId, PostState.ACTIVE)).thenReturn(post);
        when(commentMapper.toGetPostCommentDTOList(comments)).thenReturn(expectedDtos);

        // Act
        List<GetPostCommentDTO> result = commentService.GetPostComments(postId, userId);

        // Assert
        Assertions.assertEquals(expectedDtos, result);
        verify(postService).GetPostByStateAndId(postId, PostState.ACTIVE);
        verify(commentMapper).toGetPostCommentDTOList(comments);
    }

    @Test
    void createComment_ShouldSavePostWithNewComment() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        CreateCommentDTO dto = new CreateCommentDTO(postId, "Test content");
        
        User user = User.builder()
                .id(authorId)
                .build();
        
        Post post = Post.builder()
                .id(postId)
                .comments(new ArrayList<>())
                .build();

        when(userService.GetValidUser(authorId)).thenReturn(user);
        when(postService.GetPostByStateAndId(postId, PostState.ACTIVE)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);

        // Act
        commentService.CreateComment(dto, authorId);

        // Assert
        verify(userService).GetValidUser(authorId);
        verify(postService).GetPostByStateAndId(postId, PostState.ACTIVE);
        verify(postRepository).save(post);
        
        Assertions.assertEquals(1, post.getComments().size());
        Comment addedComment = post.getComments().get(0);
        Assertions.assertEquals(user, addedComment.getAuthor());
        Assertions.assertEquals(post, addedComment.getPost());
        Assertions.assertNotNull(addedComment.getDatePublished());
    }
}
