package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.CreateCommentDTO;
import com.crisd.comet.dto.output.GetPostCommentDTO;
import com.crisd.comet.mappers.CommentMapper;
import com.crisd.comet.model.Comment;
import com.crisd.comet.model.Post;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.PostState;
import com.crisd.comet.repositories.PostRepository;
import com.crisd.comet.services.interfaces.ICommentService;
import com.crisd.comet.services.interfaces.IPostService;
import com.crisd.comet.services.interfaces.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService implements ICommentService {

    private final IPostService postService;
    private final IUserService userService;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<GetPostCommentDTO> GetPostComments(UUID postId, UUID userId) {
        Post post = postService.GetPostByStateAndId(postId, PostState.ACTIVE);
        List<Comment> postComments = post.getComments();

        return commentMapper.toGetPostCommentDTOList(postComments);
    }

    @Override
    public void CreateComment(CreateCommentDTO createCommentDTO, UUID authorId) {
        User user = userService.GetValidUser(authorId);
        Post post = postService.GetPostByStateAndId(createCommentDTO.post_id(), PostState.ACTIVE);

        Comment comment = Comment.builder()
                .author(user)
                .post(post)
                .datePublished(LocalDateTime.now())
                .build();

        post.getComments().add(comment);

        postRepository.save(post);
    }
}
