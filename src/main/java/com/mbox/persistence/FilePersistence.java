package com.mbox.persistence;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mbox.config.AmazonProperties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FilePersistence {

  private static final Logger LOGGER = LoggerFactory.getLogger(FilePersistence.class);

  private AmazonS3 s3client;

  @Autowired
  AmazonProperties storageServiceConfiguration;

  @PostConstruct
  private void initializeAmazon() {
    AWSCredentials credentials = new BasicAWSCredentials(storageServiceConfiguration.getAccessKey(),
        storageServiceConfiguration.getSecretKey());
    this.s3client = new AmazonS3Client(credentials);
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();
    return convFile;
  }

  private String generateFileName(MultipartFile multiPart) {
    return multiPart.getOriginalFilename().replace(" ", "_");
  }

  private void uploadFileTos3bucket(String fileName, File file) {
    s3client.putObject(storageServiceConfiguration.getBucketName(),
        fileName, file);
  }

  public String upload(MultipartFile multipartFile, String owner) {
    LOGGER.info("upload called");
    String fileUrl = "";
    String fileName = generateFileName(multipartFile);
    try {
      File file = convertMultiPartToFile(multipartFile);

      //fileUrl = owner + "/" +fileName;
      fileUrl = fileName;
      LOGGER.info("upload called with fileUrl{}", fileUrl);
      uploadFileTos3bucket(fileName, file);
      file.delete();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return fileUrl;
  }

  public LinkedHashMap<String, String> delete(String fileUrl) {
    LOGGER.info("delete called");
    String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    LOGGER.info("delete called with filename{} complete filename{}", fileName, storageServiceConfiguration.getBucketName() + "/");
    s3client.deleteObject(new DeleteObjectRequest(storageServiceConfiguration.getBucketName() + "/", fileName));
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    map.put("status","Successfully deleted");
    return map;
  }

}
