package com.mbox.util;

import com.mbox.config.StorageServiceProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { JWTUtil.class, StorageServiceProperties.class})
@ContextConfiguration(classes = StorageServiceProperties.class)
public class JWTUtilTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtilTest.class);

  @Autowired
  JWTUtil JWTUtil;

  private static final String EMAIL_ADDRESS = "balak@gmail.com";

  @Test
  public void testCreateValid() {
    String token = JWTUtil.create(EMAIL_ADDRESS);
    try {
      String email = JWTUtil.validate(token);
      assertEquals(email,EMAIL_ADDRESS);
    } catch (Exception exception) {
      assertTrue(false);
    }
  }

  //@Test
  public void testTokenExpiry() {
    String token = JWTUtil.create(EMAIL_ADDRESS);
    try {
      Thread.sleep(16000);
      String email = JWTUtil.validate(token);
      assertTrue(false);
    } catch (Exception exception) {
      LOGGER.info("token expired, exception {}", exception.getMessage());
      assertTrue(true);
    }
  }
}
