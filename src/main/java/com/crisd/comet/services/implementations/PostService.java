package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.CreatePostDTO;
import com.crisd.comet.dto.output.GetPostDTO;
import com.crisd.comet.exceptionHandling.exceptions.ValidationException;
import com.crisd.comet.model.Post;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.PostState;
import com.crisd.comet.model.enums.PostType;
import com.crisd.comet.repositories.PostRepository;
import com.crisd.comet.services.interfaces.IMediaService;
import com.crisd.comet.services.interfaces.IPostService;
import com.crisd.comet.services.interfaces.IUserService;
import jakarta.mail.Multipart;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final IMediaService mediaService;
    private final IUserService userService;
    private final PostRepository postRepository;


    @Override
    public void CreatePost(CreatePostDTO createPostDTO) {
        User availbleUser = userService.GetValidUser(createPostDTO.userId());

        PostType postType = CheckAvailableContent(createPostDTO.text(), createPostDTO.media());

        String mediaUploadedUrl = null;
        String text = null;

        if(postType == PostType.HYBRID){
            mediaUploadedUrl = mediaService.UploadImage(createPostDTO.media());
            text = createPostDTO.text();

        }
        if(postType == PostType.TEXT_ONLY){
            text = createPostDTO.text();

        }
        if(postType == PostType.IMAGE_ONLY){
            mediaUploadedUrl = mediaService.UploadImage(createPostDTO.media());

        }
        if(postType == PostType.VIDEO_ONLY){
            mediaUploadedUrl = mediaService.UploadImage(createPostDTO.media());

        }

        Post newPost = Post.builder().author(availbleUser)
                .description(text)
                .media(mediaUploadedUrl)
                .postType(postType)
                .postState(PostState.ACTIVE)
                .datePosted(LocalDate.now())
                .reactions(new HashSet<>())
                .comments(new ArrayList<>())
                .build();

        postRepository.save(newPost);
    }

    private PostType CheckAvailableContent(String text, Multipart media){
        return PostType.HYBRID;
        // TODO: Implement method (A post can be With media and text or media only or text only.
        //  But has to have something)
    }

    @Override
    public void UpdatePost(CreatePostDTO createPostDTO) {

    }

    @Override
    public void DeletePost(UUID postId, UUID userId) {

        ChangePostState(postId, userId, PostState.ACTIVE, PostState.DELETED);
    }

    @Override
    public void ArchivePost(UUID postId, UUID userId) {

        ChangePostState(postId, userId, PostState.ACTIVE, PostState.ARCHIVED);
    }

    @Override
    public void UnarchivePost(UUID postId, UUID userId) {
        ChangePostState(postId, userId, PostState.ARCHIVED, PostState.ACTIVE);

    }

    private void ChangePostState(UUID postId, UUID userId, PostState stateToLookFor, PostState stateToChage ){
        User availableUser = userService.GetValidUser(userId);
        Post currentActivePost =  GetPostByStateAndId(postId, stateToLookFor);

        if(!currentActivePost.getAuthor().getId().equals(availableUser.getId())){
            throw new ValidationException("You do not have permission to modify this post");
        }

        currentActivePost.setPostState(stateToChage);

        postRepository.save(currentActivePost);

    }

    @Override
    public List<GetPostDTO> GetArchivedPosts(UUID userId) {
        return List.of();
    }

    @Override
    public GetPostDTO GetPost(UUID postId, UUID userId) {
        return null;
    }

    @Override
    public List<GetPostDTO> GetUserPosts(UUID userId) {
        return List.of();
    }

    @Override
    public List<GetPostDTO> GetRecommended() {
        return List.of();
    }

    @Override
    public Post GetPostByStateAndId(UUID postId, PostState postState) {
        return null;
    }
}
