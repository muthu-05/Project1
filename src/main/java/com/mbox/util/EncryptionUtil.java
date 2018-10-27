package com.mbox.util;

import com.mbox.config.StorageServiceProperties;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Component
public class EncryptionUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtil.class);

  @Autowired
  StorageServiceProperties storageServiceProperties;

  private static final String EMPTY = "";
  private static final String UNICODE_FORMAT = "UTF8";
  private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
  private static final String ALGORITHM = "AES";

  private Cipher desCipher;
  private boolean initialized;

  public String encrypt(String plaintText) {
    try {
      SecretKeySpec secretKey = new SecretKeySpec(storageServiceProperties.getEncryptionKey().getBytes(), ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);

      byte[] encValue = cipher.doFinal(plaintText.getBytes());
      String encryptedValue = new BASE64Encoder().encode(encValue);
      return encryptedValue;

    } catch (Exception exception) {
      LOGGER.error("exception occured while initializing the encryption{}", exception.getMessage());
    }
    return EMPTY;
  }

  public String decrypt(String encryptedString) {
    try {
      SecretKeySpec secretKey = new SecretKeySpec(storageServiceProperties.getEncryptionKey().getBytes(), ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);

      byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedString);
      byte[] decValue = cipher.doFinal(decordedValue);
      String decryptedValue = new String(decValue);

      return decryptedValue;
    } catch (Exception exception) {
      LOGGER.error("exception occured while initializing the encryption{}", exception.getMessage());
    }
    return EMPTY;
  }
}
