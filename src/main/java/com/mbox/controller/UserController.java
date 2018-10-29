package com.mbox.controller;


import com.mbox.exception.StorageServiceException;
import com.mbox.model.User;
import com.mbox.service.UserService;
import com.mbox.util.EncryptionUtil;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  @Autowired
  UserService userService;

  @Autowired
  EncryptionUtil encryptionUtil;

  @PostMapping
  public LinkedHashMap<String, String> createUser(
      @RequestParam(value = "email") String email,
      @RequestParam(value = "firstname") String firstName,
      @RequestParam(value = "lastname") String lastName,
      @RequestParam(value = "password") String password) {
    LOGGER.info("create user called");
    User user = new User();
    String encryptedPassword = encryptionUtil.encrypt(password);
    user = user.setEmail(email).
        setFirstname(firstName).
        setLastname(lastName).
        setPassword(encryptedPassword);
    userService.createUser(user);
    LinkedHashMap<String, String> map = userService.generateTokenResponse(email);
    return map;
  }

  @GetMapping
  public LinkedHashMap<String, String> authenticate(@RequestParam(value = "email") String email,
      @RequestParam(value = "password") String password)
    throws StorageServiceException {
    LOGGER.info("authenticate called");
    LinkedHashMap<String, String> map = userService.validateUser(email, encryptionUtil.encrypt(password));
    if (userService.isAdmin(email)) {
      map.put("admin", "true");
    }
    return map;
  }
}
