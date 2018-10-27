package com.mbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends StorageServiceException {
  public AuthenticationException(String exception) {
    super(exception);
  }
}
