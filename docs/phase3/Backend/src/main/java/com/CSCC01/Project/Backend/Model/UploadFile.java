package com.CSCC01.Project.Backend.Model;

import com.CSCC01.Project.Backend.Controllers.InputBeans.UploadBean;
import com.CSCC01.Project.Backend.Model.Lucene.LuceneModel;
import com.CSCC01.Project.Backend.Model.SchemaBeans.User;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.CSCC01.Project.Backend.Constants.PATH;

public class UploadFile {

    public static void upload(User user, UploadBean uploadBean) throws Exception {

        MultipartFile file = uploadBean.getFile();

        // Get the title and the ext
        String fileName = file.getOriginalFilename();
        String[] fileNameSplit = fileName.split("\\.");

        String title = fileNameSplit[0];
        String ext = "";

        try {
            ext = fileNameSplit[1];
        }
        catch (Exception e){
            System.out.println(e);
        }

        // Write the file to disk
        byte[] bytes = file.getBytes();
        Path path = Paths.get(PATH + fileName);
        Files.write(path, bytes);

        LuceneModel.addDocument(PATH + fileName, title, ext, uploadBean, user);
    }
}
