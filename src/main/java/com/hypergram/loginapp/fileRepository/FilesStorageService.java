package com.hypergram.loginapp.fileRepository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    public void init();

    public boolean isFilePublic(String fileOwnerName);

    public void save(MultipartFile file);

    public Resource load(String filename);

    Resource loadPublic(String filename);

    boolean deletePicture(String filename);

    boolean deletePictureAsAdminOrMod(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();

    public Stream<Path> loadAllPublic();

    public boolean isDirectorySet(Path path);

    public boolean isImageSaved(String imageId);

    public String getFileOwner(String imageId);

    public boolean isFileUserFollowedOrPublic(String fileOwnerName);
}