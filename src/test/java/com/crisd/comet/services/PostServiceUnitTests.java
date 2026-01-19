package com.crisd.comet.services;

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
import com.crisd.comet.repositories.PostRepository;
import com.crisd.comet.services.implementations.PostService;
import com.crisd.comet.services.interfaces.IMediaService; 
import com.crisd.comet.services.interfaces.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceUnitTests {

    @Mock
    private IMediaService mediaService;

    @Mock
    private IUserService userService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostsMapper postsMapper;

    @InjectMocks
    private PostService postService;

    @Test
    void testCreatePost_Hybrid_Success() {
        UUID userId = UUID.randomUUID();
        String text = "Test Post";
        List<MultipartFile> media = List.of(mock(MultipartFile.class));
        CreatePostDTO createPostDTO = new CreatePostDTO(text, media);
        User user = new User();
        user.setId(userId);

        when(userService.GetValidUser(userId)).thenReturn(user);
        when(mediaService.UploadSeveralImages(anyList())).thenReturn(new ArrayList<>());
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.CreatePost(createPostDTO, userId);

        verify(userService).GetValidUser(userId);
        verify(mediaService).UploadSeveralImages(media);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testCreatePost_TextOnly_Success() {
        UUID userId = UUID.randomUUID();
        String text = "Test Post";
        List<MultipartFile> media = Collections.emptyList();
        CreatePostDTO createPostDTO = new CreatePostDTO(text, media);
        User user = new User();
        user.setId(userId);

        when(userService.GetValidUser(userId)).thenReturn(user);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.CreatePost(createPostDTO, userId);

        verify(mediaService, never()).UploadSeveralImages(anyList());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testCreatePost_ImageOnly_Success() {
        UUID userId = UUID.randomUUID();
        String text = "";
        List<MultipartFile> media = List.of(mock(MultipartFile.class));
        CreatePostDTO createPostDTO = new CreatePostDTO(text, media);
        User user = new User();
        user.setId(userId);

        when(userService.GetValidUser(userId)).thenReturn(user);
        when(mediaService.UploadSeveralImages(anyList())).thenReturn(new ArrayList<>());
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        postService.CreatePost(createPostDTO, userId);

        verify(mediaService).UploadSeveralImages(media);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testCreatePost_ValidationException() {
        UUID userId = UUID.randomUUID();
        CreatePostDTO createPostDTO = new CreatePostDTO("", Collections.emptyList());
        User user = new User();
        user.setId(userId);

        when(userService.GetValidUser(userId)).thenReturn(user);

        assertThrows(ValidationException.class, () -> postService.CreatePost(createPostDTO, userId));
    }

    @Test
    void testUpdatePost_Success() {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        String text = "Updated Text";
        List<MultipartFile> media = List.of(mock(MultipartFile.class));
        UpdatePostDTO updatePostDTO = new UpdatePostDTO(userId, postId, text, media); 

        User user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setId(postId);
        post.setMedia(new ArrayList<>());
        
        // Mock GetPostByStateAndId logic
        when(userService.GetValidUser(userId)).thenReturn(user);
        when(postRepository.findPostByIdAndPostState(postId, PostState.ACTIVE)).thenReturn(Optional.of(post));
        when(mediaService.UploadSeveralImages(anyList())).thenReturn(new ArrayList<>());
        when(postRepository.save(any(Post.class))).thenReturn(post);

        postService.UpdatePost(updatePostDTO, userId);

        verify(postRepository).save(post);
    }
    
    @Test
    void testUpdatePost_MediaLimitExceeded() {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        UpdatePostDTO updatePostDTO = new UpdatePostDTO(userId, postId, "text", Collections.nCopies(6, mock(MultipartFile.class)));

        User user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setId(postId);
        // Simulate existing images
        List<Image> existingMedia = new ArrayList<>();
        for(int i=0; i<5; i++) existingMedia.add(new Image());
        post.setMedia(existingMedia);

        when(userService.GetValidUser(userId)).thenReturn(user);
        when(postRepository.findPostByIdAndPostState(postId, PostState.ACTIVE)).thenReturn(Optional.of(post));

        assertThrows(ValidationException.class, () -> postService.UpdatePost(updatePostDTO, userId));
    }

    @Test
    void testDeletePost_Success() {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setId(postId);
        post.setAuthor(user);
        post.setPostState(PostState.ACTIVE);

        when(userService.GetValidUser(userId)).thenReturn(user);
        when(postRepository.findPostByIdAndPostState(postId, PostState.ACTIVE)).thenReturn(Optional.of(post));

        postService.DeletePost(postId, userId);

        assertEquals(PostState.DELETED, post.getPostState());
        verify(postRepository).save(post);
    }

    @Test
    void testDeletePost_PermissionDenied() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        
        User user = new User();
        user.setId(userId);
        
        User author = new User();
        author.setId(otherUserId);
        
        Post post = new Post();
        post.setId(postId);
        post.setAuthor(author);
        post.setPostState(PostState.ACTIVE);

        when(userService.GetValidUser(userId)).thenReturn(user);
        when(postRepository.findPostByIdAndPostState(postId, PostState.ACTIVE)).thenReturn(Optional.of(post));

        assertThrows(ValidationException.class, () -> postService.DeletePost(postId, userId));
    }

    @Test
    void testArchivePost_Success() {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setId(postId);
        post.setAuthor(user);
        post.setPostState(PostState.ACTIVE);

        when(userService.GetValidUser(userId)).thenReturn(user);
        when(postRepository.findPostByIdAndPostState(postId, PostState.ACTIVE)).thenReturn(Optional.of(post));

        postService.ArchivePost(postId, userId);

        assertEquals(PostState.ARCHIVED, post.getPostState());
        verify(postRepository).save(post);
    }

    @Test
    void testUnarchivePost_Success() {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setId(postId);
        post.setAuthor(user);
        post.setPostState(PostState.ARCHIVED);

        when(userService.GetValidUser(userId)).thenReturn(user);
        when(postRepository.findPostByIdAndPostState(postId, PostState.ARCHIVED)).thenReturn(Optional.of(post));

        postService.UnarchivePost(postId, userId);

        assertEquals(PostState.ACTIVE, post.getPostState());
        verify(postRepository).save(post);
    }

    @Test
    void testRemoveImageFromPost_Success() {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        String imageId = UUID.randomUUID().toString();
        
        Post post = new Post();
        post.setId(postId);
        
        Image image = new Image();
        image.setId(imageId);
        image.setPost(post);
        
        post.setMedia(new ArrayList<>(List.of(image)));
        
        RemoveImageDTO dto = new RemoveImageDTO(postId, imageId);

        when(postRepository.findPostByIdAndPostState(postId, PostState.ACTIVE)).thenReturn(Optional.of(post));
        when(mediaService.GetImage(imageId)).thenReturn(image);

        postService.RemoveImageFromPost(userId, dto);

        assertTrue(post.getMedia().isEmpty());
        verify(postRepository).save(post);
    }
    
    @Test
    void testRemoveImageFromPost_ImageNotBelongToPost() {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        String imageId = UUID.randomUUID().toString();
        
        Post post = new Post();
        post.setId(postId);
        
        Post otherPost = new Post();
        otherPost.setId(UUID.randomUUID());
        
        Image image = new Image();
        image.setId(imageId);
        image.setPost(otherPost);
        
        RemoveImageDTO dto = new RemoveImageDTO(postId, imageId);

        when(postRepository.findPostByIdAndPostState(postId, PostState.ACTIVE)).thenReturn(Optional.of(post));
        when(mediaService.GetImage(imageId)).thenReturn(image);

        assertThrows(ValidationException.class, () -> postService.RemoveImageFromPost(userId, dto));
    }

    @Test
    void testGetPost_Success() {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        
        User user = new User();
        user.setId(userId);

        Post post = new Post();
        post.setId(postId);
        post.setAuthor(user);
        post.setPostState(PostState.ACTIVE);

        when(userService.GetValidUser(userId)).thenReturn(user);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postsMapper.toGetPostsDto(any())).thenReturn(mock(GetPostDTO.class));

        GetPostDTO result = postService.GetPost(postId, userId);

        assertNotNull(result);
    }

    @Test
    void testGetPost_NotFound() {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();

        when(userService.GetValidUser(userId)).thenReturn(new User());
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> postService.GetPost(postId, userId));
    }

    @Test
    void testGetPost_Deleted() {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        Post post = new Post();
        post.setPostState(PostState.DELETED);

        when(userService.GetValidUser(userId)).thenReturn(new User());
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThrows(EntityNotFoundException.class, () -> postService.GetPost(postId, userId));
    }

     @Test
    void testGetUserPosts() {
        UUID userId = UUID.randomUUID();
        int page = 0;
        int size = 10;
        User user = new User();
        user.setId(userId);

        List<Post> posts = List.of(new Post(), new Post());
        Page<Post> postsPage = new PageImpl<>(posts);

        when(userService.GetValidUser(userId)).thenReturn(user);
        when(postRepository.findPostsByAuthor_IdAndPostState(eq(userId), eq(PostState.ACTIVE), any(Pageable.class)))
                .thenReturn(postsPage);
        when(postsMapper.toGetPostsDtoList(posts)).thenReturn(List.of(mock(GetPostDTO.class), mock(GetPostDTO.class)));

        List<GetPostDTO> result = postService.GetUserPosts(userId, page, size);

        assertEquals(2, result.size());
    }

    @Test
    void testGetRecommended() {
        int page = 0;
        int size = 10;
        String orderBy = "datePosted";
        
        List<Post> posts = List.of(new Post(), new Post());
        Page<Post> postsPage = new PageImpl<>(posts);

        when(postRepository.findPostsByPostState(eq(PostState.ACTIVE), any(Pageable.class)))
                .thenReturn(postsPage);
        when(postsMapper.toGetPostsDtoList(posts)).thenReturn(List.of(mock(GetPostDTO.class), mock(GetPostDTO.class)));

        List<GetPostDTO> result = postService.GetRecommended(page, size, orderBy);

        assertEquals(2, result.size());
    }
}
