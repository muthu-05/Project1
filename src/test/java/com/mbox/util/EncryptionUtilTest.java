package com.mbox.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.mbox.config.StorageServiceProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { EncryptionUtil.class, StorageServiceProperties.class})
@ContextConfiguration(classes = StorageServiceProperties.class)
public class EncryptionUtilTest {

  @Autowired
  EncryptionUtil encryptionUtil;

  private static final String TEST_STRING = "balak@gmail.com";

  @Test
  public void contextLoads() {
    String encryptedString = encryptionUtil.encrypt(TEST_STRING);
    String email = encryptionUtil.decrypt(encryptedString);
    assertEquals(TEST_STRING, email);
  }
}
