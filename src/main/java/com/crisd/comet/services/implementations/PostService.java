package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.CreatePostDTO;
import com.crisd.comet.dto.input.RemoveImageDTO;
import com.crisd.comet.dto.input.UpdatePostDTO;
import com.crisd.comet.dto.output.GetPostDTO;
import com.crisd.comet.exceptionHandling.exceptions.EntityNotFoundException;
import com.crisd.comet.exceptionHandling.exceptions.ValidationException;
import com.crisd.comet.mappers.PostsMapper;
import com.crisd.comet.model.Image;
import com.crisd.comet.model.Post;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.PostState;
import com.crisd.comet.model.enums.PostType;
import com.crisd.comet.repositories.PostRepository;
import com.crisd.comet.services.interfaces.IMediaService;
import com.crisd.comet.services.interfaces.IPostService;
import com.crisd.comet.services.interfaces.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final IMediaService mediaService;
    private final IUserService userService;
    private final PostRepository postRepository;
    private final PostsMapper postsMapper;


    @Override
    public void CreatePost(CreatePostDTO createPostDTO, UUID userId) {
        User availbleUser = userService.GetValidUser(userId);

        PostType postType = CheckAvailableContent(createPostDTO.text(), createPostDTO.media());

        List<Image> mediaUploadedUrl = new ArrayList<>();
        String text = null;

        if(postType == PostType.HYBRID){
            mediaUploadedUrl = mediaService.UploadSeveralImages(createPostDTO.media());
            text = createPostDTO.text();

        }
        if(postType == PostType.TEXT_ONLY){
            text = createPostDTO.text();

        }
        if(postType == PostType.IMAGE_ONLY){
            mediaUploadedUrl = mediaService.UploadSeveralImages(createPostDTO.media());

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

    private PostType CheckAvailableContent(String text, List<MultipartFile> media){

        if(text.isBlank() && media.isEmpty()) throw new ValidationException("Invalid data, try it again");

        if(!text.isBlank() && !media.isEmpty()) return PostType.HYBRID;

        if(!text.isBlank() && media.isEmpty()) return PostType.TEXT_ONLY;

        return PostType.IMAGE_ONLY;
    }

    @Override
    public void UpdatePost(UpdatePostDTO updatePostDTO, UUID userId) {
        userService.GetValidUser(userId);
        Post postToEdit = GetPostByStateAndId(updatePostDTO.postId(), PostState.ACTIVE);

        if (postToEdit.getMedia().size() + updatePostDTO.media().size() >= 10){
            throw  new ValidationException("A post can contain just 5 images or less");
        }

        PostType postType = CheckAvailableContent(updatePostDTO.text(), updatePostDTO.media());


        List<Image> mediaUploadedUrl = new ArrayList<>();
        String text = null;

        if(postType == PostType.HYBRID){
            mediaUploadedUrl = mediaService.UploadSeveralImages(updatePostDTO.media());
            text = updatePostDTO.text();

        }
        if(postType == PostType.TEXT_ONLY){
            text = updatePostDTO.text();

        }
        if(postType == PostType.IMAGE_ONLY){
            mediaUploadedUrl = mediaService.UploadSeveralImages(updatePostDTO.media());

        }

        postToEdit.getMedia().addAll(mediaUploadedUrl);
        postToEdit.setDescription(text);

        postRepository.save(postToEdit);
    }

    @Override
    public void DeletePost(UUID postId, UUID userId) {

        ChangePostState(postId, userId, PostState.ACTIVE, PostState.DELETED);
    }

    @Override
    public void RemoveImageFromPost(UUID userId, RemoveImageDTO removeImageDTO) {
        Post postToEdit = GetPostByStateAndId(removeImageDTO.postId(), PostState.ACTIVE);
        Image image = mediaService.GetImage(removeImageDTO.imageId());
        if (!image.getPost().getId().equals(postToEdit.getId()))
            throw new ValidationException("Can't delete image from post");

        postToEdit.getMedia().remove(image);

        postRepository.save(postToEdit);
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
    public List<GetPostDTO> GetArchivedPosts(UUID userId, int page, int size) {
        return GetUserPostsByState(userId, PostState.ARCHIVED, page, size);
    }

    @Override
    public GetPostDTO GetPost(UUID postId, UUID userId) {
        userService.GetValidUser(userId);
        Optional<Post> userPost = postRepository.findById(postId);

        Post post = userPost.orElseThrow(()-> new EntityNotFoundException("Post not found"));

        if(post.getPostState() == PostState.DELETED)
            throw new EntityNotFoundException("Post not found");

        if(post.getAuthor().getId().equals(userId)){
            return postsMapper.toGetPostsDto(post);
        }

        if(post.getPostState() == PostState.ARCHIVED)
            throw new EntityNotFoundException("Post not found");

        return postsMapper.toGetPostsDto(post);
    }

    @Override
    public List<GetPostDTO> GetUserPosts(UUID userId, int page, int size) {
        return GetUserPostsByState(userId, PostState.ACTIVE, page, size);
    }

    @Override
    public List<GetPostDTO> GetRecommended(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        Page<Post> postsPage= postRepository.findPostsByPostState( PostState.ACTIVE, pageable);
        List<Post> postsList = postsPage.getContent();
        return postsMapper.toGetPostsDtoList(postsList);
    }

    private List<GetPostDTO> GetUserPostsByState(UUID userId, PostState postState, int page, int size){
        userService.GetValidUser(userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("datePosted").descending());
        Page<Post> postsPage= postRepository.findPostsByAuthor_IdAndPostState(userId, postState, pageable);
        List<Post> postsList = postsPage.getContent();
        return postsMapper.toGetPostsDtoList(postsList);
    }

    @Override
    public Post GetPostByStateAndId(UUID postId, PostState postState) {
        Optional<Post> postOptional = postRepository.findPostByIdAndPostState(postId, postState);
        return postOptional.orElseThrow(()-> new EntityNotFoundException("Post not found"));

    }
}
