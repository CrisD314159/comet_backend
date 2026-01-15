package com.crisd.comet.controllers;

import com.crisd.comet.dto.input.CreateCommentDTO;
import com.crisd.comet.dto.output.EntityResponseMessage;
import com.crisd.comet.dto.output.GetPostCommentDTO;
import com.crisd.comet.security.UserDetailsImpl;
import com.crisd.comet.services.interfaces.ICommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @GetMapping("/post/{post_id}")
    public ResponseEntity<List<GetPostCommentDTO>> GetPostComment(@PathVariable UUID post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<GetPostCommentDTO> comments = commentService.GetPostComments(post_id, userDetails.getId());
        return ResponseEntity.ok().body(comments);
    }


    @PostMapping
    public ResponseEntity<EntityResponseMessage> CreatePost(@Valid @RequestBody CreateCommentDTO createCommentDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.CreateComment(createCommentDTO, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new EntityResponseMessage(true, "Comment created"));
    }



}
