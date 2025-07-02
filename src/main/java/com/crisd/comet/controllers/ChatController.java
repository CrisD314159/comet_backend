package com.crisd.comet.controllers;

import com.crisd.comet.dto.output.EntityResponseMessage;
import com.crisd.comet.dto.output.GetUserChatToken;
import com.crisd.comet.security.UserDetailsImpl;
import com.crisd.comet.services.interfaces.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final IChatService chatService;

    @GetMapping
    public ResponseEntity<GetUserChatToken> GetUserChatToken(@AuthenticationPrincipal UserDetailsImpl user){
        UUID userId = user.getId();
        var token = chatService.generateToken(userId);
        return ResponseEntity.ok(new GetUserChatToken(token));
    }
}
