package com.hypergram.loginapp.controllers.authEntry;

import com.hypergram.loginapp.fileRepository.FilesStorageService;
import com.hypergram.loginapp.model.FileInfo;
import com.hypergram.loginapp.model.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/files")
public class FilesControllerAuth {

    @Autowired
    FilesStorageService storageService;


    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.save(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesControllerAuth.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url,storageService.getFileOwner(filename));
        }).filter(f -> storageService.isFileUserFollowedOrPublic(f.getFileOwner())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }


    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadPublic(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/filesRemove/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> removeFile(@PathVariable String filename){
        boolean success = storageService.deletePicture(filename);
        if(success){
            return ResponseEntity.ok("File removed");
        }
        else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server error");
        }
    }
    @PreAuthorize("(hasRole('MODERATOR') or hasRole('ADMIN'))")
    @GetMapping("/modAdmin/filesRemove/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> removeFileAsAdmin(@PathVariable String filename){
        boolean success = storageService.deletePictureAsAdminOrMod(filename);
        if(success){
            return ResponseEntity.ok("File removed");
        }
        else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Picture doesn't exist");
        }
    }
}