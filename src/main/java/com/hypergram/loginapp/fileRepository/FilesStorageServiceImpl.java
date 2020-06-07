package com.hypergram.loginapp.fileRepository;

import com.hypergram.loginapp.model.ImageDB;
import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.repository.ImageRepository;
import com.hypergram.loginapp.repository.UserRepository;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
            setDirectory(getUserPathByToken());
            Files.copy(file.getInputStream(),this.getUserPathByToken().resolve(filename));
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
            Path file = getUserPathByToken().resolve(filename);
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
    public Resource loadPublic(String filename) {
        try {

            Optional<ImageDB> imageDB = imageRepository.findById(filename.replace(".jpg",""));
            Path file =getUserPathByUserName(imageDB.get().getUser().getUsername()).resolve(filename);
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
    public boolean deletePicture(String filename){
        try {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<ImageDB> imageDB = imageRepository.findById(filename.replace(".jpg",""));
            Path file =getUserPathByUserName(imageDB.get().getUser().getUsername()).resolve(filename);
            if(userDetails.getUsername().equals(imageDB.get().getUser().getUsername())){
                    FileSystemUtils.deleteRecursively(file);
                    imageRepository.delete(imageDB.get());
                    return true;
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"This is not your picture");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean deletePictureAsAdminOrMod(String filename){
        try {
            Optional<ImageDB> imageDB = imageRepository.findById(filename.replace(".jpg",""));
            Path file =getUserPathByUserName(imageDB.get().getUser().getUsername()).resolve(filename);
            FileSystemUtils.deleteRecursively(file);
            imageRepository.delete(imageDB.get());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            Path userPath = getUserPathByToken();
            return Files.walk(root, 2).filter(path -> !path.equals(root)&& path.getFileName().toString().endsWith(".jpg")).map(root::relativize);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load the files!");
        }
    }
    @Override
    public Stream<Path> loadAllPublic() {
        try {
            return Files.walk(root, 2).filter(path -> !path.equals(root)&& path.getFileName().toString().endsWith(".jpg")).map(root::relativize);
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

    @Override
    public String getFileOwner(String imageId) {
        Optional<ImageDB> imageDB = imageRepository.findById(imageId.replace(".jpg",""));
        User user = imageDB.get().getUser();
        String username = user.getUsername();
        return username;
    }

    private Path createImagePath(String imageId){
        Path imagePath= Paths.get(this.getUserPathByToken()+"/"+imageId);
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
    private Path getUserPathByToken(){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Path userPath = Paths.get(root.toString()+"/"+username);

        return userPath;
    }
    private Path getUserPathByUserName(String username){
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

    @Override
    public boolean isFilePublic(String fileOwnerName){
        Optional<User> user = userRepository.findByUsername(fileOwnerName);
        return user.filter(value -> !value.isPrivateAccount()).isPresent();
    }
    public boolean isFileUserFollowedOrPublic(String fileOwnerName){
        Optional<User> fileOwner = userRepository.findByUsername(fileOwnerName);
        boolean isPublic =  fileOwner.filter(value -> !value.isPrivateAccount()).isPresent();
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        boolean hasFollow = false;
        if(user.isPresent()){
            hasFollow = user.get().isFollowingUser(fileOwnerName);
        }
        boolean isFileOwner = false;
        if(user.isPresent() && fileOwner.isPresent()){
            isFileOwner=user.get().getUsername().equals(fileOwnerName);
        }
        return hasFollow || isPublic || isFileOwner;
    }
}