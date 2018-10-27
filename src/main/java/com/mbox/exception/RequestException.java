package com.mbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestException extends StorageServiceException {
  public RequestException(String exception) {
    super(exception);
  }
}
