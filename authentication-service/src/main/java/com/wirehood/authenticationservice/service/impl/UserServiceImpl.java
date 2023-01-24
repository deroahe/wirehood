package com.wirehood.authenticationservice.service.impl;

import com.wirehood.authenticationservice.exception.UserNotFoundException;
import com.wirehood.authenticationservice.entity.User;
import com.wirehood.authenticationservice.repository.UserRepository;
import com.wirehood.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) throws UserNotFoundException {
        User user = userRepository.findByUsernameAndPassword(username, password);

        if (user == null) {
            throw new UserNotFoundException("Username or password invalid");
        }

        return user;
    }
}
