package com.mbox.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySource("classpath:application.properties")
public class StorageServiceProperties {

    @Value("${server.encryption.key}")
    private String encryptionKey;

    @Value("${server.token.secret}")
    private String tokenSectet;

    @Value("${server.token.issuer}")
    private String tokenIssuer;

    @Value("${server.token.lifetimeinms}")
    private long lifetimeInMS;

    public long getLifetimeInMS() {
        return lifetimeInMS;
    }

    public void setLifetimeInMS(long lifetimeInMS) {
        this.lifetimeInMS = lifetimeInMS;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getTokenSectet() {
        return tokenSectet;
    }

    public void setTokenSectet(String tokenSectet) {
        this.tokenSectet = tokenSectet;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public void setTokenIssuer(String tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }
}