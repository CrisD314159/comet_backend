package com.crisd.comet.integration;

import com.crisd.comet.dto.input.CreatePostDTO;
import com.crisd.comet.dto.output.GetPostDTO;
import com.crisd.comet.model.User;
import com.crisd.comet.model.enums.PostState;
import com.crisd.comet.model.enums.PostType;
import com.crisd.comet.model.enums.UserState;
import com.crisd.comet.repositories.PostRepository;
import com.crisd.comet.repositories.UserRepository;
import com.crisd.comet.services.implementations.PostService;
import com.crisd.comet.services.interfaces.IMediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
public class PostServiceIntegrationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IMediaService mediaService;

    private User testUser;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .name("testuser")
                .email("test@example.com")
                .password("password")
                .userPosts(new ArrayList<>())
                .isVerified(true)
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(testUser);
    }

    @Test
    void testCreatePost_TextOnly() {
        CreatePostDTO createPostDTO = new CreatePostDTO("Hello World", List.of());

        postService.CreatePost(createPostDTO, testUser.getId());

        assertThat(postRepository.count()).isEqualTo(1);
        var posts = postRepository.findAll();
        assertThat(posts.get(0).getDescription()).isEqualTo("Hello World");
        assertThat(posts.get(0).getPostType()).isEqualTo(PostType.TEXT_ONLY);
        assertThat(posts.get(0).getAuthor().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void testCreatePost_Hybrid() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        List<MultipartFile> media = List.of(file);
        CreatePostDTO createPostDTO = new CreatePostDTO("Hybrid Post", media);

        when(mediaService.UploadSeveralImages(anyList())).thenReturn(new ArrayList<>());

        postService.CreatePost(createPostDTO, testUser.getId());

        assertThat(postRepository.count()).isEqualTo(1);
        var posts = postRepository.findAll();
        assertThat(posts.get(0).getDescription()).isEqualTo("Hybrid Post");
        assertThat(posts.get(0).getPostType()).isEqualTo(PostType.HYBRID);
    }

    @Test
    void testGetPost() {
        CreatePostDTO createPostDTO = new CreatePostDTO("To Retrieve", List.of());
        postService.CreatePost(createPostDTO, testUser.getId());
        var savedPost = postRepository.findAll().get(0);

        GetPostDTO retrievedPost = postService.GetPost(savedPost.getId(), testUser.getId());

        assertThat(retrievedPost).isNotNull();
        assertThat(retrievedPost.description()).isEqualTo("To Retrieve");
    }

    @Test
    void testDeletePost() {
        CreatePostDTO createPostDTO = new CreatePostDTO("To Delete", List.of());
        postService.CreatePost(createPostDTO, testUser.getId());
        var savedPost = postRepository.findAll().get(0);

        postService.DeletePost(savedPost.getId(), testUser.getId());

        var deletedPost = postRepository.findById(savedPost.getId()).orElseThrow();
        assertThat(deletedPost.getPostState()).isEqualTo(PostState.DELETED);
    }

    @Test
    void testArchiveAndUnarchivePost() {
        CreatePostDTO createPostDTO = new CreatePostDTO("To Archive", List.of());
        postService.CreatePost(createPostDTO, testUser.getId());
        var savedPost = postRepository.findAll().get(0);

        // Archive
        postService.ArchivePost(savedPost.getId(), testUser.getId());
        var archivedPost = postRepository.findById(savedPost.getId()).orElseThrow();
        assertThat(archivedPost.getPostState()).isEqualTo(PostState.ARCHIVED);

        // Unarchive
        postService.UnarchivePost(savedPost.getId(), testUser.getId());
        var unarchivedPost = postRepository.findById(savedPost.getId()).orElseThrow();
        assertThat(unarchivedPost.getPostState()).isEqualTo(PostState.ACTIVE);
    }

    @Test
    void testGetUserPosts() {
        postService.CreatePost(new CreatePostDTO("Post 1", List.of()), testUser.getId());
        postService.CreatePost(new CreatePostDTO("Post 2", List.of()), testUser.getId());

        List<GetPostDTO> userPosts = postService.GetUserPosts(testUser.getId(), 0, 10);

        assertThat(userPosts).hasSize(2);
    }
}
