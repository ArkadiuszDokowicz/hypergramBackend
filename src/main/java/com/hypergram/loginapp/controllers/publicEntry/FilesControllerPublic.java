package com.hypergram.loginapp.controllers.publicEntry;

import com.hypergram.loginapp.controllers.authEntry.FilesControllerAuth;
import com.hypergram.loginapp.fileRepository.FilesStorageService;
import com.hypergram.loginapp.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/public/files")
public class FilesControllerPublic {
    @Autowired
    FilesStorageService storageService;


    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAllPublic().map(path -> {
            String filename = path.getFileName().toString();

            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesControllerPublic.class, "getFile", path.getFileName().toString()).build().toString();
            String fileUser = storageService.getFileOwner(filename);
            return new FileInfo(filename, url,fileUser);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadPublic(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
