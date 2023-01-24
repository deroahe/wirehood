package com.wirehood.authenticationservice.service;

import com.wirehood.authenticationservice.exception.UserNotFoundException;
import com.wirehood.authenticationservice.entity.User;

public interface UserService {

    public void saveUser(User user);

    public User getUserByUsernameAndPassword(String username, String password) throws UserNotFoundException;
}
