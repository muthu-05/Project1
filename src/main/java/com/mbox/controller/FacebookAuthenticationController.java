package com.mbox.controller;

import com.mbox.config.FacebookProperties;
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
/*
https://stackoverflow.com/questions/37001004/facebook-login-message-url-blocked-this-redirect-failed-because-the-redirect
Facebook Login -> Settings -> Valid OAuth redirect URIs
 */
@RestController
@RequestMapping("/authentication/facebook")
public class FacebookAuthenticationController {
  private static final Logger LOGGER = LoggerFactory.getLogger(FacebookAuthenticationController.class);

  @Autowired
  FacebookProperties facebookProperties;

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
    LOGGER.info("facebook login called");

    String loginUrl = authenticationService.getUrl(facebookProperties.getLoginUrl(),
        facebookProperties.getClientId(),
        facebookProperties.getClientSecret(),
        facebookProperties.getScope(),
        facebookProperties.getResponseType(),
        facebookProperties.getRedirectUrl(),
        redirectUrl);
    response.sendRedirect(loginUrl);
  }

  @RequestMapping(value = "/loginredirect",
      method= RequestMethod.GET)
  public void loginRedirect(HttpServletResponse response,
      @RequestParam(value = "code") String code,
      @RequestParam(value = "state") String state)
    throws IOException {
    LOGGER.info("facebook login redirect called");
    LinkedHashMap<String, Object> map = authenticationService.doGetToken(
        facebookProperties.getTokenUrl(),
        code,
        facebookProperties.getClientId(),
        facebookProperties.getClientSecret(),
        facebookProperties.getRedirectUrl(),
        facebookProperties.getGrantType(),
        facebookProperties.getContentType());
    LinkedHashMap<String, String> profileMap = profileService.getProfile(facebookProperties.getProfileUrl(), map);
    String token = "";
    if (profileMap.get("email") != null) {
      token = jwtUtil.create(profileMap.get("email"));
    }
    response.sendRedirect(authenticationService.getAppRedirectUrl(state, token));
  }

}
