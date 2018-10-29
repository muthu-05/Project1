package com.mbox.controller;

import com.mbox.config.GoogleProperties;
import com.mbox.service.AuthenticationService;
import com.mbox.service.ProfileService;
import com.mbox.util.JWTUtil;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication/google")
public class GoogleAuthenticationController {
  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleAuthenticationController.class);

  @Autowired
  GoogleProperties googleProperties;

  @Autowired
  AuthenticationService authenticationService;

  @Autowired
  ProfileService profileService;

  @Autowired
  JWTUtil jwtUtil;

  @RequestMapping(value = "/login",
      method= RequestMethod.GET)
  public void login(HttpServletResponse response,
      @RequestParam(value = "redirecturl") String redirectUrl)
      throws IOException{
    LOGGER.info("google login called");

    String loginUrl = authenticationService.getUrl(googleProperties.getLoginUrl(),
        googleProperties.getClientId(),
        googleProperties.getClientSecret(),
        googleProperties.getScope(),
        googleProperties.getResponseType(),
        googleProperties.getRedirectUrl(),
        redirectUrl);
    response.sendRedirect(loginUrl);
  }

  @RequestMapping(value = "/loginredirect",
      method= RequestMethod.GET)
  public void loginRedirect(HttpServletResponse response,
      @RequestParam(value = "code") String code,
      @RequestParam(value = "state") String state)
    throws IOException {
    LOGGER.info("google login redirect called");
    LinkedHashMap<String, Object> map = authenticationService.doPostToken(
        googleProperties.getTokenUrl(),
        code,
        googleProperties.getClientId(),
        googleProperties.getClientSecret(),
        googleProperties.getRedirectUrl(),
        googleProperties.getGrantType(),
        googleProperties.getContentType());
    LinkedHashMap<String, String> profileMap = profileService.getProfile(googleProperties.getProfileUrl(), map);
    String token = "";
    if (profileMap.get("emailAddress") != null) {
      token = jwtUtil.create(profileMap.get("emailAddress"));
    }
    response.sendRedirect(authenticationService.getAppRedirectUrl(state, token));
  }

}
