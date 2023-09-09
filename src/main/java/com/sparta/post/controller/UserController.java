package com.sparta.post.controller;

import com.sparta.post.dto.LoginRequestDto;
import com.sparta.post.dto.SignupRequestDto;
import com.sparta.post.entity.Message;
import com.sparta.post.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    // ResponseEntity<Map>
    @PostMapping("/auth/signup")
    public ResponseEntity<Message> signup(@Valid @RequestBody SignupRequestDto requestDto){
        return userService.signup(requestDto);
    }

    @DeleteMapping("/auth/escape")
    public ResponseEntity<Message> escape(){
        return userService.escape();
    }
}
