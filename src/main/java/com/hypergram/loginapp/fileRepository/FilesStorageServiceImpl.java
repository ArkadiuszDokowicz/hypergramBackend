package com.hypergram.loginapp.fileRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import com.hypergram.loginapp.model.ImageDB;
import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.repository.ImageRepository;
import com.hypergram.loginapp.repository.UserRepository;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    UserRepository userRepository;

    private final Path root = Paths.get("uploads");
    private final String fileExtension = ".jpg";
    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        String filename;
        try {
            filename = createFilename();
            filename = filename + fileExtension;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        try {
            setDirectory(getUserPath());
            Files.copy(file.getInputStream(),this.getUserPath().resolve(filename));
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            Optional<User> user = userRepository.findByUsername(username);
            imageRepository.save(new ImageDB(getFileNameWithoutExtension(filename),user.get()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = getUserPath().resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            Path userPath = getUserPath();
            return Files.walk(userPath, 1).filter(path -> !path.equals(userPath)).map(userPath::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public boolean isDirectorySet(Path path){
        return Files.exists(path);
    }

    @Override
    public boolean isImageSaved(String imageId) {
        Path imagePath= Paths.get(createImagePath(imageId)+fileExtension);
        return isDirectorySet(imagePath);
    }

    private Path createImagePath(String imageId){
        Path imagePath= Paths.get(this.getUserPath()+"/"+imageId);
        return imagePath;
    }
    private void setDirectory(Path path){
        if(!isDirectorySet(path)){
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileNameWithoutExtension(String filename){
        String filenameWithoutExtension = filename.replace(fileExtension,"");
        return filenameWithoutExtension;
    }
    private Path getUserPath(){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Path userPath = Paths.get(root.toString()+"/"+username);

        return userPath;
    }
    private String createFilename() throws NoSuchAlgorithmException {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        StringBuilder filename = new StringBuilder();
        filename.append(username);
        filename.append(date.toString());
        return createHashForFile(filename.toString());
    }
    public String createHashForFile(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        String sha256hex = new String(Hex.encode(hash));
        return sha256hex;
    }
}