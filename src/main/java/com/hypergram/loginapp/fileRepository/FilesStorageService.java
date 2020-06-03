package com.hypergram.loginapp.fileRepository;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
    public void init();

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
}