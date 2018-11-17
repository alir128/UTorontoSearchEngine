package com.CSCC01.Project.Backend.Controllers.Resources;

import com.CSCC01.Project.Backend.Controllers.InputBeans.SearchBean;
import com.CSCC01.Project.Backend.Controllers.InputBeans.UploadBean;
import com.CSCC01.Project.Backend.Controllers.OutputBeans.DocumentBean;
import com.CSCC01.Project.Backend.Model.Lucene.LuceneModel;
import com.CSCC01.Project.Backend.Model.Repositories.GroupRepository;
import com.CSCC01.Project.Backend.Model.Repositories.UserRepository;
import com.CSCC01.Project.Backend.Model.SchemaBeans.Group;
import com.CSCC01.Project.Backend.Model.SchemaBeans.User;
import com.CSCC01.Project.Backend.Model.UploadFile;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    // public search
    @PostMapping(value = "/search")
    public List<DocumentBean> publicSearch(@ModelAttribute SearchBean searchBean) throws Exception {

        // Set the access to public
        ArrayList<String> newAccessGroups = new ArrayList<>();
        newAccessGroups.add("public");
        searchBean.setAccessGroups(newAccessGroups);

        ArrayList<Document> documents = LuceneModel.search(searchBean);
        return LuceneModel.getDocBeans(documents);
    }

    // User search
    @PostMapping(value = "/users/{id}/search")
    public List<DocumentBean> userSearch(@ModelAttribute SearchBean searchBean,
                                         @PathVariable String id) throws Exception {

        // Get the user
        User user = userRepository.findByEmail(id);

        // todo - auth

        ArrayList<Document> documents =  LuceneModel.search(searchBean);
        return LuceneModel.getDocBeans(documents);
    }

    // User upload
    @PostMapping(value = "/users/{id}/upload")
    public void userUpload(@ModelAttribute UploadBean uploadBean,
                           @PathVariable String id) throws Exception {

        // Get the user
        User user = userRepository.findByEmail(id);

        // todo - auth

        UploadFile.upload(user, uploadBean);
//        System.out.println(uploadBean);
    }

    // add users
    @PostMapping(value = "/users")
    public void addUser(@ModelAttribute User user) {
        userRepository.save(user);
    }

    // admin route - add group
    @PostMapping(value = "/admin/groups")
    public void addGroup(@ModelAttribute Group group) {
        groupRepository.save(group);
    }

    // admin route
    @GetMapping(value = "/admin/users/list")
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    // admin route
    @GetMapping(value = "/admin/groups/list")
    public List<Group> listGroups() {
        return groupRepository.findAll();
    }

    // admin route
    @GetMapping(value = "/admin/groups/clear")
    public void clearGroups() {
        groupRepository.deleteAll();
    }

    // admin route
    @GetMapping(value = "/admin/users/clear")
    public void clearUsers() {
        userRepository.deleteAll();
    }

}
