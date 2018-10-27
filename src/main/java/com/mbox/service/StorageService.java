package com.mbox.service;

import com.mbox.exception.RequestException;
import com.mbox.exception.StorageServiceException;
import com.mbox.model.FileMetaData;
import com.mbox.persistence.FileMetaDataRepository;
import com.mbox.persistence.FilePersistence;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageService {

  @Autowired
  private FileMetaDataRepository fileMetaDataRepository;

  @Autowired
  private FilePersistence filePersistence;

  public FileMetaData upload(MultipartFile file, String title, String owner) {
    String url = this.filePersistence.upload(file);

    return createFileMetadata(title, owner, url);
  }

  public String delete(String owner, String id)
    throws StorageServiceException {
    FileMetaData fileMetaData = fileMetaDataRepository.findByIdForOwner(owner, Long.parseLong(id));
    if (fileMetaData == null) {
      throw new RequestException("record not found");
    }
    fileMetaDataRepository.delete(fileMetaData);
    return this.filePersistence.delete(fileMetaData.getUrl());
  }

  public List<FileMetaData> files(String owner) {
    return fileMetaDataRepository.findAllForOwner(owner);
  }

  private FileMetaData createFileMetadata(String title, String owner, String url) {
    FileMetaData fileMetaData = new FileMetaData();
    fileMetaData.setTitle(title).setOwner(owner).setUrl(url);
    fileMetaData  = fileMetaDataRepository.save(fileMetaData);
    return fileMetaData;
  }
}
