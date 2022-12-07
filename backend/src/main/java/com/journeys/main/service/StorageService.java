/**
 * @team Journeys
 * @file StorageService.java
 * @date January 21st, 2022
 */

package com.journeys.main.service;

import com.journeys.main.config.FileStorageProperties;
import com.journeys.main.exceptions.storage.FileUploadException;
import com.journeys.main.exceptions.storage.StorageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Transactional
@Service
public class StorageService {

    private Path storageLocation;

    /**
     * Initialization of storage
     * @param prop
     * @throws Exception
     */
    @Autowired
    public void init(FileStorageProperties prop) throws Exception {
        this.storageLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.storageLocation);
        } catch (IOException e) {
            throw new Exception("Could not create directory at specified location");
        }
    }

    /**
     * Loading a
     * @param fileName
     * @return the path
     */
    public Path load(String fileName) {
        return storageLocation.resolve(fileName);
    }

    /**
     * Loading the ressource
     * @param prefix
     * @param filename the name of the file
     * @return a Ressource
     * @throws StorageException
     */
    public Resource loadAsRessource(String prefix,String poi_id, String filename) throws StorageException {

        try {
            Path file = load(prefix.isEmpty() ? filename : prefix + "/" + poi_id +"/"+ filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }

    /**
     * Storing a ressource to a directory
     * @param file the file
     * @param targetFilename the name of the file
     * @param location the path
     * @throws FileUploadException
     */
    public void store(MultipartFile file, String targetFilename, String location) throws FileUploadException {

        String filename = StringUtils.cleanPath(targetFilename);
        try {
            String directoryPath = this.storageLocation.toAbsolutePath() + "/" + location;
            File f = new File(directoryPath + filename);
            if (!f.exists() && !f.isDirectory()) {
                Path uploadPath = Paths.get(directoryPath);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                if (filename.contains(".."))
                    throw new FileUploadException("Illegal file");

                Path target = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException io) {
            throw new FileUploadException("Could not store file with filename " + filename);
        }
    }

    /**
     * Clear target directory and stores new files
     * @param files
     * @param location
     * @param target
     * @return
     */
    public List<String> replaceJourneysImages(List<MultipartFile> files, String location, String target){
        File folder = new File(this.storageLocation.toAbsolutePath() + "/"+location);

        List<String> file_names = new ArrayList<>();
        List<File> listofImages = new ArrayList<>();
        //delete existing files
        if(!folder.isDirectory() && !folder.exists()){
            try {
                Files.createDirectories(Paths.get(folder.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            File poiFolder = new File(this.storageLocation.toAbsolutePath() + "/"+location+"/"+target);
            if(!poiFolder.isDirectory() && !poiFolder.exists()){
                try {
                    Files.createDirectories(Paths.get(poiFolder.getAbsolutePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            listofImages = Arrays.stream(Objects.requireNonNull(poiFolder.listFiles())).collect(Collectors.toList());
            listofImages.forEach(file ->{
                if(file.exists() && file.isFile()){
                    file_names.add(file.getName());
                }
            });
        }


        //upload new images
        AtomicInteger i = new AtomicInteger(listofImages.size());
        if(files != null){
            for(MultipartFile file: files){

                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String target_name = target+"-"+i.get()+"."+extension;

                store(file,target_name,location+"/"+target);
                file_names.add(target_name);

                i.incrementAndGet();
            }
        }
        return file_names;
    }
}