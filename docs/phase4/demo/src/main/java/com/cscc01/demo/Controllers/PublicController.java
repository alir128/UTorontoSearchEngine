package com.cscc01.demo.Controllers;


import com.cscc01.demo.Controllers.PublicOutputBeans.ChartsResponse;

import com.cscc01.demo.Controllers.PublicOutputBeans.ResultObject;
import com.cscc01.demo.Models.DataPersistence.FileUpload;
import com.cscc01.demo.Models.Lucene.LuceneBeans.DocumentWithHighlights;
import com.cscc01.demo.Models.MongoUser.MongoUser;
import com.cscc01.demo.Models.MongoUser.MongoUserDetailsService;
import com.cscc01.demo.Models.Lucene.LuceneIndex;
import com.cscc01.demo.Models.Lucene.LuceneProcess;
import com.cscc01.demo.Models.Lucene.LuceneSearch;
import com.cscc01.demo.Models.Repositories.FileRepository;
import com.cscc01.demo.Models.Repositories.GroupRepository;
import com.cscc01.demo.Models.Repositories.UserRepository;
import com.cscc01.demo.Models.SchemaBeans.Group;
import com.cscc01.demo.Models.SchemaBeans.User;
import com.cscc01.demo.Models.Tika.ParsedFile;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.cscc01.demo.Constants.PATH;

@Controller
public class PublicController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    private MongoUserDetailsService userService;

    @GetMapping("/")
    public String root() {
        return "public/publicHome";
    }

    @GetMapping("/search")
    public String getSearch() {
        return "public/publicHome";
    }
    public ArrayList<ResultObject> searchprocess (String query, Principal principal) throws Exception {
    	// Search Documents
        ArrayList<DocumentWithHighlights> documents;

        

        // If the user is null, make a multifield (Occur.SHOULD) search with public as the viewType.
        if (principal == null) {
            documents = LuceneSearch.multiSearch(
                    query,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "public",
                    Integer.MAX_VALUE,
                    BooleanClause.Occur.MUST
            );
        }

        else {

            // Auth boilerplate
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            MongoUser mongoUser = (MongoUser) auth.getPrincipal();
            String userId = mongoUser.getId();
            // Identify the user
            User user = userRepository.findUserById(userId);

            // Go through all the user's groups and make a string of the group codes.
            String groupCodes = "";
            for (String groupId: user.getGroupIds()) {
                // Get the group
                Group group = groupRepository.findGroupById(groupId);
                // Add to the groupCodes
                groupCodes += " " + group.getCode();
            }
            // Remove the beginning space
            groupCodes = groupCodes.length() == 0 ? groupCodes : groupCodes.substring(1);

            String viewType = "";
            // If the user is Student, set public and student as viewType
            if (user.getType().equals("Student")) {
                viewType = "student public";
            }

            // If the user is Instructor, set public and instructor as viewType
            if (user.getType().equals("Instructor")) {
                viewType = "instructor public";
            }

            // Perform the multifield search
            documents = LuceneSearch.multiSearch(
                    query,
                    "",
                    groupCodes,
                    user.getUsername(),
                    "",
                    "",
                    "",
                    viewType,
                    Integer.MAX_VALUE,
                    BooleanClause.Occur.SHOULD
            );
        }

        // documents -> resultObjects
        ArrayList<ResultObject> resultObjects = LuceneProcess.fromDocuments(documents);
        return resultObjects;
    	
    }
    @PostMapping("/search")
    @ResponseBody
    public ModelAndView postSearch(@RequestParam("searchQuery") String query,
//                             @RequestParam("maxHits") Integer maxHits,
                             Model model, HttpServletResponse response,
                                   Principal principal
    ) throws Exception {

    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MongoUser mongoUser = (MongoUser) auth.getPrincipal();
        String userId = mongoUser.getId();
        // Identify the user
        User user = userRepository.findUserById(userId);

        // get saved file list
        String fileIdsStr = "";
        Set<String> fileIds = user.getFileIds() == null ? new HashSet<>() : user.getFileIds();
        for (String fileId : fileIds) {
            fileIdsStr += fileId + ",";
        }
    	ArrayList<ResultObject> resultObjects = searchprocess(query,principal);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("public/publicResults");
        modelAndView.addObject("resultObjects",resultObjects);
        ChartsResponse stats = chartInfo(resultObjects);
        modelAndView.addObject("stats",stats);
        modelAndView.addObject("query",query);

        // add saved files list to model as well
        modelAndView.addObject("fileIdsStr", fileIdsStr);

        return modelAndView;
    }

    @GetMapping("/searchajax/")
    @ResponseBody
    public ModelAndView postSearchajax(@RequestParam("searchQuery") String query, HttpServletRequest request,
//                             @RequestParam("maxHits") Integer maxHits,
                             Model model, HttpServletResponse response, Principal principal
    ) throws Exception {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MongoUser mongoUser = (MongoUser) auth.getPrincipal();
        String userId = mongoUser.getId();
        // Identify the user
        User user = userRepository.findUserById(userId);

        // get saved file list
        String fileIdsStr = "";
        Set<String> fileIds = user.getFileIds() == null ? new HashSet<>() : user.getFileIds();
        for (String fileId : fileIds) {
            fileIdsStr += fileId + ",";
        }
    	ArrayList<ResultObject> resultObjects = searchprocess(query,principal);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("public/results");
        modelAndView.addObject("resultObjects",resultObjects);
        ChartsResponse stats = chartInfo(resultObjects);
        modelAndView.addObject("stats",stats);
        modelAndView.addObject("query",query);
        // add saved files list to model as well
        modelAndView.addObject("fileIdsStr", fileIdsStr);
        return modelAndView;
    }

    public static ChartsResponse chartInfo(ArrayList<ResultObject> resultObjects) {
    	ChartsResponse response = new ChartsResponse();
    	Map<String, Integer> data = new HashMap<String,Integer>();
    	Iterator<ResultObject> resultObject = resultObjects.iterator();
    	while(resultObject.hasNext()) {
    		ResultObject result = resultObject.next();
    		Integer typeCount= data.get(result.getFileType());
            if (typeCount == null) {
            	data.put(result.getFileType(), 1);
            }
            else {
            	data.put(result.getFileType(), typeCount+1);
            }
    	}
    	response.setFiletype(data);
		return response;
    }

    @GetMapping("/upload")
    public ModelAndView getUpload() {
    	ModelAndView modelAndView = new ModelAndView();
   	 	modelAndView.setViewName("public/upload");
   	 	List<String> groups = getAllUserGroups();
   	 	modelAndView.addObject("groups", groups);
   	 	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
   	 	MongoUser currentUser = (MongoUser) auth.getPrincipal();
   	 	if(currentUser.getType().toLowerCase().contains("student")) {
   	 		modelAndView.addObject("student",currentUser.getType().toLowerCase());
   	 	} else {
   	 		modelAndView.addObject("instructor",currentUser.getType().toLowerCase());
   	 	}
        return modelAndView;
    }

    public List<String> getAllUserGroups() {

        List<String> returnSet = new ArrayList<String>();

        // Get the current user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MongoUser currentUser = (MongoUser) auth.getPrincipal();
        String userId = currentUser.getId();

        User user = userService.findUserById(userId);

        // Go through each group in the user and the group name to the returnSet
        Iterator<String> grpids = user.getGroupIds().iterator();
        while(grpids.hasNext()) {
        	 Group group = groupRepository.findGroupById(grpids.next());
        	 returnSet.add(group.getCode());
        }

        // return results
        return returnSet;
    }

    @PostMapping("/upload")
    public ModelAndView postUpload(@RequestParam("file") MultipartFile file,
                             @RequestParam(value = "tags", required = false, defaultValue = "") ArrayList<String> tags,
                             @RequestParam(value = "groups", required = false, defaultValue = "") ArrayList<String> groupCodes,
                             @RequestParam(value = "viewType", defaultValue = "") String viewType
                             ) throws Exception {

    	// If groups and viewType are empty, set viewType to private
    	if (groupCodes.isEmpty() && viewType.equals("")) {
    		viewType = "private";
    	}

    	ModelAndView modelAndView = new ModelAndView();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	MongoUser currentUser = (MongoUser)auth.getPrincipal();


    	String username = auth.getName();
        // get id
        String id = UUID.randomUUID().toString();

        //save to user also
    	User user = userService.findUserById(currentUser.getId());

    	Set<String> files = user.getFileIds();
        files.add(id);
        user.setFileIds(files);
        System.out.println(files.toString());
        userRepository.save(user);

        //set date and time
        Date currentDateObject = new Date();

        // file upload
        FileUpload.save(file);

        // get the file
        File getFile = new File(PATH + file.getOriginalFilename());
        // parse the file to get the body and the filetype
        ParsedFile parsedFile = new ParsedFile(getFile);
        String fileType = parsedFile.getFileType().split(";")[0];

        // lucene indexing
        LuceneIndex.indexDocument(
                file,
                id,
                username,
                new SimpleDateFormat("hh:mm").format(currentDateObject),
                new SimpleDateFormat("yyMMdd").format(currentDateObject),
                tags,
                groupCodes,
                fileType,
                viewType,
                parsedFile.getBody()
        );

        // create a new file
        com.cscc01.demo.Models.SchemaBeans.File dbFile = new com.cscc01.demo.Models.SchemaBeans.File(
                id,
                file.getOriginalFilename(),
                fileType,
                tags,
                username,
                new SimpleDateFormat("hh:mm").format(currentDateObject),
                new SimpleDateFormat("yy-MM-dd").format(currentDateObject)
        );
        // Set viewType
        dbFile.setViewType(viewType);

        // Add all the groupIds to the dbFile
        for (String groupCode: groupCodes) {

            // Get the Group object
            Group group = groupRepository.findByCode(groupCode);

            // Add the groupId to the file
            dbFile.addGroup(group);

            // Add the fileId to the group
            group.addFile(dbFile);

            // Save the group
            groupRepository.save(group);
        }

        fileRepository.save(dbFile);
        modelAndView.setViewName("public/upload");
		modelAndView.addObject("successMessage", "File was uploaded successfully.");
        return modelAndView;
    }

    @RequestMapping(value = "/file")
    public ResponseEntity<Resource> postUpload(@RequestParam("id") String id) throws Exception {

        // get document
        DocumentWithHighlights documentWithHighlights = LuceneSearch.searchOther(id, "id", 1).get(0);
        Document document = documentWithHighlights.getDocument();

        // get the file
        File file = new File(document.get("path"));

        // get the path
        Path path = Paths.get(document.get("path"));
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        HttpHeaders headers = new HttpHeaders(); headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + document.get("file"));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @GetMapping("/viewfile")
    public String postUpload(@RequestParam("id") String id,
                             Model model) throws Exception {
        // get document
    	DocumentWithHighlights documentWithHighlights = LuceneSearch.searchOther(id, "id", 1).get(0);

        // document -> resultObjects
        ResultObject resultObject = LuceneProcess.fullContent(documentWithHighlights);

        // add to model
        model.addAttribute("resultObject", resultObject);

        return "public/publicFileInfo";
    }

    @GetMapping("/help")
    public String help(Model model) throws Exception {

        return "public/help";
    }

    @GetMapping(value = "viewgrp")
    @ResponseBody
    public List<Group> listGroups(HttpServletRequest request,
//          @RequestParam("maxHits") Integer maxHits,
          Model model, HttpServletResponse response)  throws Exception {
    	 ModelAndView modelAndView = new ModelAndView();
    	 modelAndView.setViewName("public/publicResults");
    	 modelAndView.addObject("resultObjects", groupRepository.findAll());

    	return groupRepository.findAll();
    }
}
