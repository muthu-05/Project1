package com.mbox.controller;

import com.mbox.config.FacebookProperties;
import com.mbox.service.AuthenticationService;
import com.mbox.service.ProfileService;
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
@RequestMapping("/authentication/facebook")
public class FacebookAuthenticationController {
  private static final Logger LOGGER = LoggerFactory.getLogger(FacebookAuthenticationController.class);

  @Autowired
  FacebookProperties facebookProperties;

  @Autowired
  AuthenticationService authenticationService;

  @Autowired
  ProfileService profileService;

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
    response.sendRedirect(authenticationService.getAppRedirectUrl(state, code));
  }

  @RequestMapping(value = "/token",
      method= RequestMethod.GET)
  public LinkedHashMap<String, String> token(@RequestParam(value = "code") String code) {
    LOGGER.info("facebook token called");

    LinkedHashMap<String, Object> map = authenticationService.doGetToken(
        facebookProperties.getTokenUrl(),
        code,
        facebookProperties.getClientId(),
        facebookProperties.getClientSecret(),
        facebookProperties.getRedirectUrl(),
        facebookProperties.getGrantType(),
        facebookProperties.getContentType());
    return profileService.getProfile(facebookProperties.getProfileUrl(), map);
  }

}
