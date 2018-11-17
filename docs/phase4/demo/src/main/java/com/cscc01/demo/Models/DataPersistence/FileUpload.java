package com.cscc01.demo.Models.DataPersistence;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.cscc01.demo.Constants.PATH;

public class FileUpload {

    public static void save(MultipartFile file) throws Exception {

        // Write the file to disk
        byte[] bytes = file.getBytes();
        Path path = Paths.get(PATH + file.getOriginalFilename());
        Files.write(path, bytes);
    }
}
