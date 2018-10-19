package com.mbox.service;

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

  public String delete(String id) {
    FileMetaData fileMetaData = fileMetaDataRepository.findById(Long.parseLong(id)).get();
    fileMetaDataRepository.delete(fileMetaData);
    return this.filePersistence.delete(fileMetaData.getUrl());
  }

  public List<FileMetaData> files() {
    List<FileMetaData> list = new ArrayList<>();
    fileMetaDataRepository.findAll().forEach(e -> list.add(e));
    return list;
  }

  private FileMetaData createFileMetadata(String title, String owner, String url) {
    FileMetaData fileMetaData = new FileMetaData();
    fileMetaData.setTitle(title).setOwner(owner).setUrl(url);
    fileMetaData  = fileMetaDataRepository.save(fileMetaData);
    return fileMetaData;
  }
}
