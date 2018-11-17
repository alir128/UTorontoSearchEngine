package com.cscc01.demo.Controllers;


import com.cscc01.demo.Models.Lucene.LuceneBeans.DocumentWithHighlights;
import com.cscc01.demo.Models.MongoUser.MongoUser;
import com.cscc01.demo.Models.Repositories.FileRepository;
import com.cscc01.demo.Models.Repositories.GroupRepository;
import com.cscc01.demo.Models.Repositories.UserRepository;
import com.cscc01.demo.Models.SchemaBeans.File;
import com.cscc01.demo.Models.Lucene.LuceneSearch;
import com.cscc01.demo.Models.SchemaBeans.User;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import org.springframework.ui.Model;
import javax.servlet.http.HttpServletResponse;
import com.cscc01.demo.Controllers.PublicOutputBeans.ResultObject;
import com.cscc01.demo.Models.Lucene.LuceneProcess;
import com.cscc01.demo.Controllers.PublicOutputBeans.ChartsResponse;



@Controller
public class TestController {

	@Autowired
	FileRepository fileRepository;

	@Autowired
    GroupRepository groupRepository;

	@Autowired
    UserRepository userRepository;


    @GetMapping("test/username/search")
    @ResponseBody
    public String usernameSearch(@RequestParam("username") String username
//                                 @RequestParam("maxHits") Integer maxHits
    ) throws Exception {

        // get the Documents
        ArrayList<DocumentWithHighlights> documents = LuceneSearch.searchOther(username, "owner", 10);

        // make a string of titles and return it
        String result = "";
        for (DocumentWithHighlights documentWithHighlights: documents) {
            Document document = documentWithHighlights.getDocument();
            result += document.get("file") + " ";
        }

        return result;
    }

    @GetMapping("users/getallfiletypes")
    @ResponseBody
    public Map<String, Integer> getAllFileTypesFromUser() throws Exception {

    	Map<String, Integer> fileTypeMap = new HashMap<>();

    	// get the current logged in username
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	String username = auth.getName();

    	// get all the files associated with the username
    	ArrayList<File> files = (ArrayList<File>) fileRepository.findByOwner(username);

    	// go through all the files and add increment the content type
    	for (File file: files) {

    		// get the content type
    		String type = file.getContentType();

    		// add to the fileTypeMap
    		// if it contains, increment
    		if (fileTypeMap.containsKey(type)) {
    			fileTypeMap.put(type, fileTypeMap.get(type) + 1);
    		}
    		// if it doesnt, set it to 1
    		else {
    			fileTypeMap.put(type, 1);
    		}
    	}

    	// return the map
    	return fileTypeMap;
    }


		@RequestMapping(value="/save", method=RequestMethod.POST)
		@ResponseBody
		// @PostMapping("/save")
		// public void addToFavourites(String fileId) {
		public void addToFavourites(@RequestParam String id) {
				// String fileId = obj.id;
				String fileId = id;
				System.out.println("fileId: ");
				System.out.println(fileId);
        // Auth boiler plate
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MongoUser currentMongoUser = (MongoUser) auth.getPrincipal();
        String currentUserId = currentMongoUser.getId();
        User currentUser = userRepository.findUserById(currentUserId);

        // Get the User's fileId list
        Set<String> fileIds = currentUser.getFileIds() == null ? new HashSet<>() : currentUser.getFileIds();
        fileIds.add(fileId);

        // Save the User
        userRepository.save(currentUser);

        // return "redirect:/";
    }

		@RequestMapping(value="/delete", method=RequestMethod.POST)
		@ResponseBody
		public void deleteFromFavourites(@RequestParam String id) {
				String fileId = id;
				System.out.println("fileId: ");
				System.out.println(fileId);
        // Auth boiler plate
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MongoUser currentMongoUser = (MongoUser) auth.getPrincipal();
        String currentUserId = currentMongoUser.getId();
        User currentUser = userRepository.findUserById(currentUserId);

        // Get the User's fileId list
        Set<String> fileIds = currentUser.getFileIds() == null ? new HashSet<>() : currentUser.getFileIds();

				// Delete the fileId
				if (fileIds.contains(fileId)) {
						fileIds.remove(fileId);
				}

        // Save the User
        userRepository.save(currentUser);

        // return "redirect:/";
    }

		@PostMapping("/showSaved")
    @ResponseBody
    public ModelAndView postSearch(Model model, HttpServletResponse response) throws Exception {
	    	// System.out.println(fileIds.toString());
				// Auth boiler plate
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				MongoUser currentMongoUser = (MongoUser) auth.getPrincipal();
				String currentUserId = currentMongoUser.getId();
				User currentUser = userRepository.findUserById(currentUserId);

				// Get the User's fileId list
				Set<String> fileIds = currentUser.getFileIds() == null ? new HashSet<>() : currentUser.getFileIds();
				System.out.println("fileIds:");
				System.out.println(fileIds.toString());
        // lucene -> documents
				ArrayList<DocumentWithHighlights> documents = new ArrayList<>();
				if (fileIds != null && !fileIds.isEmpty()) {
					for (String fileId : fileIds) {
						ArrayList<DocumentWithHighlights> temp_documents = LuceneSearch.searchOther(fileId, "id", 1);
						if (temp_documents != null && !temp_documents.isEmpty()) {
							documents.add(temp_documents.get(0));
						}
						System.out.println("temp_documents:");
						System.out.println(temp_documents.toString());

					}
				}
				System.out.println("documents:");
				System.out.println(documents.toString());
        // documents -> resultObjects
        ArrayList<ResultObject> resultObjects = LuceneProcess.fromDocuments(documents);

        // add to model
//        model.addAttribute("resultObjects", resultObjects);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("public/savedFileShowing");
        modelAndView.addObject("resultObjects",resultObjects);
        ChartsResponse stats = chartInfo(resultObjects);
        modelAndView.addObject("stats",stats);
        // modelAndView.addObject("query",query);
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


}
