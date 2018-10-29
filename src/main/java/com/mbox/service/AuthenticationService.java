package com.mbox.service;

import com.mbox.util.JSONUtil;
import com.mbox.util.RestTemplateUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

@Component
public class AuthenticationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

  private static final String CLIENT_ID_PATTERN = "{clientid}";
  private static final String CLIENT_SECRET_PATTERN = "{clientsecret}";
  private static final String SCOPE_PATTERN = "{scope}";
  private static final String RESPONSE_TYPE_PATTERN = "{responsetype}";
  private static final String REDIRECT_URL_PATTERN = "{redirecturl}";
  private static final String STATE_PATTERN = "{state}";
  private static final String CODE_PATTERN = "{code}";

  private static final String QUESTION = "?";
  private static final String EQUAL = "=";

  private static final String TOKEN = "token";
  private static final String CODE = "code";
  private static final String CLIENT_ID = "client_id";
  private static final String CLIENT_SECRET = "client_secret";
  private static final String REDIRECT_URL = "redirect_uri";
  private static final String GRANT_TYPE = "grant_type";

  @Autowired
  JSONUtil jsonUtil;

  @Autowired
  RestTemplateUtil restTemplateUtil;

  public String getAppRedirectUrl(String state, String token) {
    return state + QUESTION + TOKEN + EQUAL + token;
  }

  public LinkedHashMap<String, Object> doPostToken(String tokenUrl,
      String code, String clientId,
      String clientSecret, String redirectUrl, String grantType,
      String contentType) {

    LinkedMultiValueMap<String, String> body = getTokenRequestBody(code,
        clientId, clientSecret, redirectUrl, grantType
      );

    LOGGER.info("body:{}", body);
    LOGGER.info("tokenUrl:{}", tokenUrl);

    HttpHeaders requestHeaders =  getTokenRequestHeaders(contentType);
    HttpEntity<?> requestEntity = new HttpEntity<>(body, requestHeaders);

    ResponseEntity<String> responseEntity = restTemplateUtil.createRestTemplate().exchange(
        tokenUrl,
        HttpMethod.POST,
        requestEntity,
        String.class
    );

    LOGGER.info("status{}", responseEntity.getStatusCode());
    if(responseEntity.getStatusCode() == HttpStatus.OK){
      LOGGER.info("response from serverr{}", responseEntity.getBody());
      return jsonUtil.parseJSON(responseEntity.getBody());
    }

    return new LinkedHashMap<String, Object>();
  }

  public LinkedHashMap<String, Object> doGetToken(String tokenUrl,
      String code, String clientId,
      String clientSecret, String redirectUrl, String grantType,
      String contentType) {

    String scope = "";
    String responseType = "";
    String state = "";

    tokenUrl = getUrl(tokenUrl, clientId, clientSecret, scope, responseType, redirectUrl, state);
    tokenUrl = tokenUrl.replace(CODE_PATTERN, code);

    LOGGER.info("tokenUrl:{}", tokenUrl);

    ResponseEntity<String> responseEntity = restTemplateUtil.createRestTemplate().exchange(
        tokenUrl,
        HttpMethod.GET,
        null,
        String.class
    );

    LOGGER.info("status{}", responseEntity.getStatusCode());
    if(responseEntity.getStatusCode() == HttpStatus.OK){
      LOGGER.info("response from serverr{}", responseEntity.getBody());
      return jsonUtil.parseJSON(responseEntity.getBody());
    }

    return new LinkedHashMap<String, Object>();
  }

  public String getUrl(String url, String clientId,
      String clientSecret, String scope, String responseType,
      String redirectUrl, String state) {
    LOGGER.info("getLoginUrl called");
    return url.replace(CLIENT_ID_PATTERN, clientId).
        replace(SCOPE_PATTERN, scope).
        replace(RESPONSE_TYPE_PATTERN, responseType).
        replace(REDIRECT_URL_PATTERN, redirectUrl).
        replace(STATE_PATTERN, state).
        replace(CLIENT_SECRET_PATTERN, clientSecret);
  }

  private LinkedMultiValueMap<String, String> getTokenRequestBody(String code,
      String clientId, String clientSecret,
      String redirectUrl, String grantType) {
    LOGGER.info("getTokenRequestBody called");
    LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

    ArrayList<String> list = new ArrayList<String>(1);
    list.add(clientId);
    map.put(CLIENT_ID, list);

    ArrayList<String> list1 = new ArrayList<String>(1);
    list1.add(clientSecret);
    map.put(CLIENT_SECRET, list1);

    ArrayList<String> list2 = new ArrayList<String>(1);
    list2.add(redirectUrl);
    map.put(REDIRECT_URL, list2);

    ArrayList<String> list3 = new ArrayList<String>(1);
    list3.add(code);
    map.put(CODE, list3);

    ArrayList<String> list4 = new ArrayList<String>(1);
    list4.add(grantType);
    map.put(GRANT_TYPE, list4);

    return map;
  }

  private HttpHeaders getTokenRequestHeaders(String contentType) {
    LOGGER.info("getTokenRequestHeaders called");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    return httpHeaders;
  }
}
