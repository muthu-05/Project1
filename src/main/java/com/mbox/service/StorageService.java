package com.mbox.service;

import com.mbox.exception.RequestException;
import com.mbox.exception.StorageServiceException;
import com.mbox.model.FileMetaData;
import com.mbox.persistence.FileMetaDataRepository;
import com.mbox.persistence.FilePersistence;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageService {
  private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);
  @Autowired
  private FileMetaDataRepository fileMetaDataRepository;

  @Autowired
  private FilePersistence filePersistence;

  public FileMetaData upload(MultipartFile file, String owner, String description) {
    LOGGER.info("upload called");
    String url = this.filePersistence.upload(file, owner);
    LOGGER.info("checking record existance");
    FileMetaData fileMetaData = fileMetaDataRepository.findByTitleForOwner(owner, file.getOriginalFilename());
    if (fileMetaData == null) {
      LOGGER.info("record not found, creating");
      return createFileMetadata(file.getOriginalFilename(), owner, description, file.getSize(), url);
    } else {
      LOGGER.info("record found, updating");
      return updateFileMetadata(fileMetaData, file.getOriginalFilename(), owner, description, file.getSize(), url);
    }
  }

  public LinkedHashMap<String, String> delete(String owner, String id)
    throws StorageServiceException {
    LOGGER.info("delete by owner called");
    return deleteInternal(owner, id);
  }

  public LinkedHashMap<String, String> delete(String id)
      throws StorageServiceException {
    LOGGER.info("delete by admin called");
    return deleteInternal(null, id);
  }

  private LinkedHashMap<String, String> deleteInternal(String owner, String id)
      throws StorageServiceException {
    LOGGER.info("delete called");
    FileMetaData fileMetaData = null;
    if (owner == null) {
      fileMetaData = fileMetaDataRepository.findByItemId(Long.parseLong(id));
    } else {
      fileMetaData = fileMetaDataRepository.findByIdForOwner(owner, Long.parseLong(id));
    }
    if (fileMetaData == null) {
      throw new RequestException("record not found");
    }
    fileMetaDataRepository.delete(fileMetaData);
    return this.filePersistence.delete(fileMetaData.getUrl());
  }

  public List<FileMetaData> files(String owner) {
    return fileMetaDataRepository.findAllForOwner(owner);
  }

  public List<FileMetaData> allFiles() {
    return fileMetaDataRepository.findAll();
  }

  private FileMetaData createFileMetadata(String title, String owner,
      String description, long size, String url) {
    LOGGER.info("createFileMetadata called");
    FileMetaData fileMetaData = new FileMetaData();
    fileMetaData.setTitle(title).setOwner(owner).setUrl(url).
        setDescription(description).setSize(size);
    fileMetaData  = fileMetaDataRepository.save(fileMetaData);
    return fileMetaData;
  }

  private FileMetaData updateFileMetadata(FileMetaData fileMetaData,
      String title, String owner,
      String description, long size, String url) {
    LOGGER.info("updateFileMetadata called");
    fileMetaData.setTitle(title).setOwner(owner).setUrl(url).
        setDescription(description).setSize(size);
    fileMetaData  = fileMetaDataRepository.save(fileMetaData);
    return fileMetaData;
  }
}
