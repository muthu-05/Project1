package com.mbox.util;

import com.mbox.exception.RequestException;
import com.mbox.service.UserService;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Component
public class TokenUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtil.class);

  public static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PART = "Bearer ";
  private static final String EMPTY = " ";

  @Autowired
  JWTUtil jwtUtil;

  @Autowired
  UserService userService;

  public String getEmailAddress(String jwtToken)
      throws RuntimeException {

    LOGGER.info("getEmailAddress called jwtToken{}", jwtToken);

    if (jwtToken == null || jwtToken.isEmpty()) {
      throw new RequestException("invalid authorization header");
    }
    jwtToken = jwtToken.replace(BEARER_PART, "");

    try {
      String email = jwtUtil.validate(jwtToken);
      LOGGER.info("retrieved email{}", email);
      try{
            userService.isValidUser(email);
            LOGGER.info("retrieved email{} is valid", email);
      }catch (Exception exception) {
        LOGGER.info("user not found maybe external system authentication is used, exception{}", exception.getMessage());
      }
      return email;
    } catch (Exception exception) {
      LOGGER.info("invalid token is passed, exception{}", exception.getMessage());
      throw new RequestException(exception.getMessage());
    }
  }
}