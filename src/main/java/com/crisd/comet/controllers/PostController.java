package com.crisd.comet.controllers;

import com.crisd.comet.dto.input.CreatePostDTO;
import com.crisd.comet.dto.input.RemoveImageDTO;
import com.crisd.comet.dto.input.UpdatePostDTO;
import com.crisd.comet.dto.output.EntityResponseMessage;
import com.crisd.comet.dto.output.GetPostDTO;
import com.crisd.comet.security.UserDetailsImpl;
import com.crisd.comet.services.interfaces.IPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;

    @GetMapping("/userPosts")
    public ResponseEntity<List<GetPostDTO>> GetUserPosts(@RequestParam int page, @RequestParam int size, @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<GetPostDTO> postDTOS = postService.GetUserPosts(userDetails.getId(), page, size);
        return ResponseEntity.ok().body(postDTOS);
    }

    @PostMapping
    public ResponseEntity<EntityResponseMessage> CreatePost(@Valid @RequestBody CreatePostDTO createPostDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.CreatePost(createPostDTO, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new EntityResponseMessage(true, "Post created"));
    }

    @PutMapping
    public ResponseEntity<EntityResponseMessage> UpdatePost(@Valid @RequestBody UpdatePostDTO updatePostDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.UpdatePost(updatePostDTO, userDetails.getId());
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "Post updated"));
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<EntityResponseMessage> DeletePost(@PathVariable UUID post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.DeletePost(post_id, userDetails.getId());
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "Post deleted"));
    }

    @PutMapping("/removeImage")
    public ResponseEntity<EntityResponseMessage> RemoveImageFromPost (@Valid @RequestBody RemoveImageDTO removeImageDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.RemoveImageFromPost(userDetails.getId(), removeImageDTO);
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "Image removed"));
    }

    @PutMapping("/archive/{post_id}")
    public ResponseEntity<EntityResponseMessage> ArchivePost (@PathVariable UUID post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.ArchivePost(post_id, userDetails.getId());
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "Post archived"));
    }

    @PutMapping("/unarchive/{post_id}")
    public ResponseEntity<EntityResponseMessage> UnarchivePost (@PathVariable UUID post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.UnarchivePost(post_id, userDetails.getId());
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "Post is now public"));
    }

    @GetMapping("/archive")
    public ResponseEntity<List<GetPostDTO>> GetArchivedPosts (@RequestParam int page, @RequestParam int size, @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<GetPostDTO> archivedPosts = postService.GetArchivedPosts(userDetails.getId(), page, size);
        return ResponseEntity.ok().body(archivedPosts);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<GetPostDTO> GetPost (@PathVariable UUID post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        GetPostDTO postDTO = postService.GetPost(post_id, userDetails.getId());
        return ResponseEntity.ok().body(postDTO);
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<GetPostDTO>> GetRecommendedPosts (@RequestParam int page, @RequestParam int size,  @RequestParam String sortBy){
        List<GetPostDTO> recommendedPosts = postService.GetRecommended(page, size, sortBy);
        return ResponseEntity.ok().body(recommendedPosts);
    }
}
