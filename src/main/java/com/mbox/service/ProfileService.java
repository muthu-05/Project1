package com.mbox.service;

import com.mbox.config.GoogleProperties;
import com.mbox.util.JSONUtil;
import com.mbox.util.RestTemplateUtil;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfileService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);

  private static final String ACCESS_TOKEN = "access_token";
  private static final String EMAIL_ADDRESS_RESPONSE = "emailAddress";
  private static final String EMAIL_ADDRESS = "emailaddress";
  private static final String AUTHORIZATION = "Authorization";
  private static final String AUTHORIZATION_VALUE_TEMPLATE = "Bearer {accesstoken}";
  private static final String ACCESS_TOKEN_PATTERN = "{accesstoken}";

  @Autowired
  JSONUtil jsonUtil;

  @Autowired
  RestTemplateUtil restTemplateUtil;

  public LinkedHashMap<String, String> getProfile(String profileUrl,
      LinkedHashMap<String, Object> tokenMap) {

    LOGGER.info("getProfile called");

    LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
    if (tokenMap.get(ACCESS_TOKEN) != null) {
      LOGGER.info("access token found");
      String accessToken = tokenMap.get(ACCESS_TOKEN).toString();

      LOGGER.info("profileUrl{}", profileUrl);

      HttpEntity<String> entity = new HttpEntity<String>(null,getProfileRequestHeaders(accessToken));

      ResponseEntity<String> responseEntity = restTemplateUtil.createRestTemplate().exchange(
          getUrl(profileUrl, accessToken),
          HttpMethod.GET,
          entity,
          String.class
      );
      LOGGER.info("status from profile server {}", responseEntity.getStatusCode());
      if(responseEntity.getStatusCode() == HttpStatus.OK){
        LOGGER.info("response from profile serverr{}", responseEntity.getBody());
        LinkedHashMap<String, Object> map = jsonUtil.parseJSON(responseEntity.getBody());
        for (String key: map.keySet()) {
          result.put(key, map.get(key).toString());
        }
      }
    }
    return result;
  }

  private HttpHeaders getProfileRequestHeaders(String accessToken) {
    LOGGER.info("getProfileRequestHeaders called");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set(AUTHORIZATION, AUTHORIZATION_VALUE_TEMPLATE.replace(ACCESS_TOKEN_PATTERN, accessToken));
    return httpHeaders;
  }

  private String getUrl(String url, String accessToken) {
    LOGGER.info("getUrl called");
    return url.replace(ACCESS_TOKEN_PATTERN, accessToken);
  }
}
