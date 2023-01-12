package com.wirehood.authenticationservice.controller;

import com.wirehood.authenticationservice.exception.UserNotFoundException;
import com.wirehood.authenticationservice.entity.User;
import com.wirehood.authenticationservice.jwt.JwtUtil;
import com.wirehood.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/authentication")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping()
    public ResponseEntity<?> postUser(@RequestBody User user){
        try{
            userService.saveUser(user);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            if(user.getUsername() == null || user.getPassword() == null) {
                throw new UserNotFoundException("Username or password invalid");
            }

            User userData = userService.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
            if(userData == null){
                throw new UserNotFoundException("Username or password invalid");
            }

            return new ResponseEntity<>(jwtUtil.generateToken(user.getUsername()), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
