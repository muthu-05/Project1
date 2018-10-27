package com.mbox.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mbox.config.StorageServiceProperties;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtil.class);

  private static final String EMAIL = "email";

  @Autowired
  StorageServiceProperties storageServiceProperties;

  private Algorithm getAlgorithm() {
    return Algorithm.HMAC256(storageServiceProperties.getTokenSectet());
  }

  private Algorithm getIssuer() {
    return Algorithm.HMAC256(storageServiceProperties.getTokenSectet());
  }

  public String create(String email) {
    Date date = new Date(new Date().getTime() + storageServiceProperties.getLifetimeInMS());
    String token = JWT.create()
        .withIssuer(storageServiceProperties.getTokenIssuer())
        .withClaim(EMAIL, email)
        .withExpiresAt(date)
        .sign(getAlgorithm());
    return token;
  }

  public String validate(String token)
    throws Exception {
    LOGGER.info("validate called");
    JWTVerifier verifier = JWT.require(getAlgorithm())
        .withIssuer(storageServiceProperties.getTokenIssuer())
        .build(); //Reusable verifier instance
    DecodedJWT jwt = verifier.verify(token);
    LOGGER.info("token verified");
    if (jwt.getClaim(EMAIL) != null) {
      return jwt.getClaim(EMAIL).asString();
    }
    LOGGER.info("email claim not found");
    return "";
  }

}
