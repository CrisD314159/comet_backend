package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.CreatePostDTO;
import com.crisd.comet.dto.output.GetPostCommentDTO;
import com.crisd.comet.dto.output.GetPostDTO;
import com.crisd.comet.model.Post;
import com.crisd.comet.model.enums.PostState;

import java.util.List;
import java.util.UUID;

public interface IPostService {
    void CreatePost(CreatePostDTO createPostDTO);
    void UpdatePost(CreatePostDTO createPostDTO);
    void DeletePost(UUID postId, UUID userId);
    void ArchivePost(UUID postId, UUID userId);
    void UnarchivePost(UUID postId, UUID userId);
    List<GetPostDTO> GetArchivedPosts(UUID userId);
    GetPostDTO GetPost(UUID postId, UUID userId);
    List<GetPostDTO> GetUserPosts(UUID userId);
    List<GetPostDTO> GetRecommended();
    Post GetPostByStateAndId(UUID postId, PostState postState);

}
