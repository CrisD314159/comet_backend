package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.CreateCommentDTO;
import com.crisd.comet.dto.output.GetPostCommentDTO;

import java.util.List;
import java.util.UUID;

public interface ICommentService {
    List<GetPostCommentDTO> GetPostComments(UUID postId, UUID userId);
    void CreateComment(CreateCommentDTO createCommentDTO, UUID author);
}
