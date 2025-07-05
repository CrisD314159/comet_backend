package com.crisd.comet.controllers;

import com.crisd.comet.dto.input.SignUpDTO;
import com.crisd.comet.dto.input.UpdateUserDTO;
import com.crisd.comet.dto.input.VerifyAccountDTO;
import com.crisd.comet.dto.output.*;
import com.crisd.comet.exceptionHandling.exceptions.EntityNotFoundException;
import com.crisd.comet.model.Friendship;
import com.crisd.comet.model.User;
import com.crisd.comet.security.UserDetailsImpl;
import com.crisd.comet.services.interfaces.IUserService;
import io.getstream.chat.java.exceptions.StreamException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;


    @PostMapping("/signUp")
    public ResponseEntity<EntityResponseMessage> CreateUser(@Valid @RequestBody SignUpDTO signUpDTO) throws MessagingException, IOException, StreamException {
        userService.SignUp(signUpDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EntityResponseMessage(true, "User Created"));
    }

    @PutMapping
    public ResponseEntity<EntityResponseMessage> UpdateUser(@Valid @RequestBody UpdateUserDTO signUpDTO) throws StreamException {
        userService.UpdateUser(signUpDTO);
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "User Updated"));
    }

    @DeleteMapping
    public ResponseEntity<EntityResponseMessage> DeleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        UUID userId = userDetails.getId();
        userService.DeleteUser(userId);
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "User Deleted"));
    }

    @GetMapping("/profile")
    public ResponseEntity<GetUserOverviewDTO> GetUserOverview(@AuthenticationPrincipal UserDetailsImpl userAuth) {
        UUID userId = userAuth.getId();
        var user = userService.GetUserAccountOverview(userId);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/getFriends")
    public ResponseEntity<GetUserFriendsDTO> GetUserFriends(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        var userFriends = userService.GetUserFriends(userId);
        return ResponseEntity.ok().body(userFriends);
    }

    @GetMapping("/getBlockedFriends")
    public ResponseEntity<GetUserFriendsDTO> GetUserBlockedFriends(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        var userFriends = userService.GetUserBlockedFriends(userId);
        return ResponseEntity.ok().body(userFriends);
    }

    @PutMapping("/verifyAccount")
    public ResponseEntity<EntityResponseMessage> VerifyAccount(@Valid @RequestBody VerifyAccountDTO verifyAccountDTO){
        userService.VerifyAccount(verifyAccountDTO);
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "Account Verified"));
    }


    @GetMapping("/search")
    public ResponseEntity<ArrayList<GetUserOverviewDTO>> SearchUsers(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam String name,
            @RequestParam Integer offset,
            @RequestParam Integer limit
    ) {
        UUID userId = userDetails.getId();
        var results = userService.SearchUsers(userId, name, offset, limit);
        return ResponseEntity.ok().body(results);
    }
}
