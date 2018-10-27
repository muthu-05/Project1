package com.mbox.service;

import com.mbox.exception.AuthenticationException;
import com.mbox.exception.RequestException;
import com.mbox.model.User;
import com.mbox.persistence.UserRepository;
import com.mbox.util.JWTUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  @Autowired
  UserRepository userRepository;

  @Autowired
  JWTUtil jwtUtil;

  public User createUser(User user) {
    User savedUser = userRepository.save(user);
    return savedUser;
  }

  public String validateUser(String email, String password)
    throws AuthenticationException {
    List<User> savedUser = userRepository.findByEmail(email);
    if (savedUser == null || savedUser.isEmpty()) {
      LOGGER.info("user not found in the system");
      throw new RequestException("invalid request");
    }
    if (savedUser.size() > 1) {
      LOGGER.info("multiple user found in the system");
      throw new RequestException("invalid request");
    }
    if (password.equals(savedUser.get(0).getPassword())) {
      LOGGER.info("user credentials matched");
      return jwtUtil.create(savedUser.get(0).getEmail());
    }
    throw new AuthenticationException("invalid user credentials");
  }

  public boolean isValidUser(String userName)
      throws AuthenticationException {

    LOGGER.info("isValidUser called");

    List<User> savedUser = userRepository.findByEmail(userName);
    if (savedUser == null) {
      LOGGER.info("user not found in the system");
      throw new RequestException("invalid request");
    }
    if (savedUser.size() > 1) {
      LOGGER.info("multiple user found in the system");
      throw new AuthenticationException("invalid user credentials");
    }
    return true;
  }

}
