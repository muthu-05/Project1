package com.mbox.controller;

import com.mbox.persistence.FileMetaDataRepository;
import com.mbox.persistence.FilePersistence;
import com.mbox.model.FileMetaData;
import com.mbox.service.StorageService;
import com.mbox.util.TokenUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    TokenUtil tokenUtil;

    @RequestMapping(value = "/file", consumes = {"multipart/form-data", "multipart/mixed"},
            method=RequestMethod.POST)
    public FileMetaData uploadFile(HttpServletRequest request,
        @RequestPart(value = "file") MultipartFile file,
        @RequestPart(value = "title") String title) {
        LOGGER.info("create file called");
        String token = request.getHeader(TokenUtil.AUTHORIZATION_HEADER);
        String owner = tokenUtil.getEmailAddress(token);
        return storageService.upload(file, title, owner);
    }

    @DeleteMapping("/file")
    public String deleteFile(HttpServletRequest request,
        @RequestParam("id") String filemetadataid) {
        LOGGER.info("delete file called");
        String token = request.getHeader(TokenUtil.AUTHORIZATION_HEADER);
        String owner = tokenUtil.getEmailAddress(token);
        return storageService.delete(owner, filemetadataid);
    }

    @GetMapping("/files")
    public List<FileMetaData> Files(HttpServletRequest request) {
        LOGGER.info("get files called");
        String token = request.getHeader(TokenUtil.AUTHORIZATION_HEADER);
        String owner = tokenUtil.getEmailAddress(token);
        return storageService.files(owner);
    }

}