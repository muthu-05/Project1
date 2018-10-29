package com.mbox.controller;

import com.mbox.persistence.FileMetaDataRepository;
import com.mbox.persistence.FilePersistence;
import com.mbox.model.FileMetaData;
import com.mbox.service.StorageService;
import com.mbox.service.UserService;
import com.mbox.util.TokenUtil;
import java.util.LinkedHashMap;
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
    UserService userService;

    @Autowired
    TokenUtil tokenUtil;

    @RequestMapping(value = "/file", consumes = {"multipart/form-data", "multipart/mixed"},
            method=RequestMethod.POST)
    public FileMetaData uploadFile(HttpServletRequest request,
        @RequestPart(value = "file") MultipartFile file,
        @RequestPart(value = "description") String description)
        throws Exception {
        LOGGER.info("create file called");
        String token = request.getHeader(TokenUtil.AUTHORIZATION_HEADER);
        String owner = tokenUtil.getEmailAddress(token);
        if (userService.isAdmin(owner)) {
            throw new Exception("admin user information passed");
        }
        return storageService.upload(file, owner, description);
    }

    @DeleteMapping("/file")
    public LinkedHashMap<String, String> deleteFile(HttpServletRequest request,
        @RequestParam("id") String filemetadataid)
        throws Exception {
        LOGGER.info("delete file called");
        String token = request.getHeader(TokenUtil.AUTHORIZATION_HEADER);
        String owner = tokenUtil.getEmailAddress(token);
        if (userService.isAdmin(owner)) {
            return storageService.delete(filemetadataid);
        } else {
            return storageService.delete(owner, filemetadataid);
        }
    }

    @GetMapping("/files")
    public List<FileMetaData> Files(HttpServletRequest request) {
        LOGGER.info("get files called");
        String token = request.getHeader(TokenUtil.AUTHORIZATION_HEADER);
        String owner = tokenUtil.getEmailAddress(token);
        if (userService.isAdmin(owner)) {
            return storageService.allFiles();
        } else {
            return storageService.files(owner);
        }
    }

}