package com.crisd.comet.controllers;

import com.crisd.comet.dto.input.SignUpDTO;
import com.crisd.comet.dto.input.UpdateUserDTO;
import com.crisd.comet.dto.output.*;
import com.crisd.comet.services.interfaces.IUserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;


    @PostMapping("/signUp")
    public ResponseEntity<EntityResponseMessage> CreateUser(@Valid @RequestBody SignUpDTO signUpDTO) throws MessagingException, IOException {
        userService.SignUp(signUpDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EntityResponseMessage(true, "User Created"));
    }

    @PutMapping
    public ResponseEntity<EntityResponseMessage> UpdateUser(@Valid @RequestBody UpdateUserDTO signUpDTO){
        userService.UpdateUser(signUpDTO);
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "User Updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityResponseMessage> DeleteUser(@PathVariable UUID id){
        userService.DeleteUser(id);
        return ResponseEntity.ok().body(new EntityResponseMessage(true, "User Deleted"));
    }

    @GetMapping
    public ResponseEntity<GetUserOverviewDTO> GetUserOverview(@PathVariable UUID id) {
        var user = userService.GetUserAccountOverview(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/getFriends")
    public ResponseEntity<GetUserFriendsDTO> GetUserFriends(@PathVariable UUID id) {
        var userFriends = userService.GetUserFriends(id);
        return ResponseEntity.ok().body(userFriends);
    }



}
