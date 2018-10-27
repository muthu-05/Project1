package com.mbox.exception;

public class StorageServiceException extends RuntimeException {
  StorageServiceException(String message) {
    super(message);
  }
}
