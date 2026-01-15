package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.CreatePostDTO;
import com.crisd.comet.dto.input.RemoveImageDTO;
import com.crisd.comet.dto.input.UpdatePostDTO;
import com.crisd.comet.dto.output.GetPostCommentDTO;
import com.crisd.comet.dto.output.GetPostDTO;
import com.crisd.comet.model.Post;
import com.crisd.comet.model.enums.PostState;

import java.util.List;
import java.util.UUID;

public interface IPostService {
    void CreatePost(CreatePostDTO createPostDTO, UUID userId);
    void UpdatePost(UpdatePostDTO updatePostDTO, UUID userId);
    void DeletePost(UUID postId, UUID userId);
    void RemoveImageFromPost(UUID userId, RemoveImageDTO removeImageDTO);
    void ArchivePost(UUID postId, UUID userId);
    void UnarchivePost(UUID postId, UUID userId);
    List<GetPostDTO> GetArchivedPosts(UUID userId, int page, int size);
    GetPostDTO GetPost(UUID postId, UUID userId);
    List<GetPostDTO> GetUserPosts(UUID userId, int page, int size);
    List<GetPostDTO> GetRecommended(int page, int size, String orderBy);
    Post GetPostByStateAndId(UUID postId, PostState postState);

}
