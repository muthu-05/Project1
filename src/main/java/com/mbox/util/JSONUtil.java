package com.mbox.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JSONUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);

  public String toJSON(Object object) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(object);
    } catch(Exception exception) {
      LOGGER.error("exception occured while converting the json, exception{}", exception.getMessage());
    }
    return "";
  }

  public LinkedHashMap<String,Object> parseJSON(String stringToBeParsed) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(stringToBeParsed, LinkedHashMap.class);
    } catch(Exception exception) {
      LOGGER.error("exception occured while converting the json, exception{}", exception.getMessage());
    }
    return new LinkedHashMap<String,Object>();
  }
}
