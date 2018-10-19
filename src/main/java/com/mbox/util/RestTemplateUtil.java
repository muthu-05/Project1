package com.mbox.util;

import com.mbox.interceptor.RequestResponseLoggingInterceptor;
import java.util.Collections;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateUtil {

  public RestTemplate createRestTemplate() {
    ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
    RestTemplate restTemplate = new RestTemplate(factory);
    restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
    return restTemplate;
  }

}
