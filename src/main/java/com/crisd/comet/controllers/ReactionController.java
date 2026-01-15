package com.crisd.comet.controllers;

import com.crisd.comet.dto.output.EntityResponseMessage;
import com.crisd.comet.security.UserDetailsImpl;
import com.crisd.comet.services.interfaces.IReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final IReactionService reactionService;

    @PostMapping("/addReaction/{post_id}")
    public ResponseEntity<EntityResponseMessage> AddReaction(@PathVariable UUID post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        reactionService.ReactToPost(post_id, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new EntityResponseMessage(true, "Reaction added"));

    }
    @DeleteMapping("/removeReaction/{post_id}")
    public ResponseEntity<EntityResponseMessage> RemoveReaction(@PathVariable UUID post_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        reactionService.RemoveReactToPost(post_id, userDetails.getId());
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "Reaction removed"));

    }

}
