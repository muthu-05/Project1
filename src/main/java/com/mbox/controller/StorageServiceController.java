package com.mbox.controller;

import com.mbox.persistence.FileMetaDataRepository;
import com.mbox.persistence.FilePersistence;
import com.mbox.model.FileMetaData;
import com.mbox.service.StorageService;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
public class StorageServiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceController.class);

    @Autowired
    StorageService storageService;

    @RequestMapping(value = "/file", consumes = {"multipart/form-data", "multipart/mixed"},
            method=RequestMethod.POST)
    public FileMetaData uploadFile(@RequestPart(value = "file") MultipartFile file,
        @RequestPart(value = "title") String title,
        @RequestPart(value = "owner") String owner) {
        LOGGER.info("create file called");
        return storageService.upload(file, title, owner);
    }

    @DeleteMapping("/file")
    public String deleteFile(@RequestParam("id") String filemetadataid) {
        LOGGER.info("delete file called");
        return storageService.delete(filemetadataid);
    }

    @GetMapping("/files")
    public List<FileMetaData> Files() {
        LOGGER.info("get files called");
        return storageService.files();
    }

}