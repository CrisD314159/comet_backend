package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.CreateCommentDTO;
import com.crisd.comet.dto.output.GetPostCommentDTO;
import com.crisd.comet.services.interfaces.ICommentService;

import java.util.List;
import java.util.UUID;

public class CommentService implements ICommentService {
    @Override
    public List<GetPostCommentDTO> GetPostComments(UUID postId, UUID userId) {
        return List.of();
    }

    @Override
    public void CreateComment(CreateCommentDTO createCommentDTO) {

    }
}
