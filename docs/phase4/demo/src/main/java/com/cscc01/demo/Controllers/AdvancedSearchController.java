package com.cscc01.demo.Controllers;

import com.cscc01.demo.Controllers.PublicOutputBeans.ResultObject;
import com.cscc01.demo.Models.Lucene.LuceneProcess;
import com.cscc01.demo.Models.Lucene.LuceneSearch;
import com.cscc01.demo.Models.MongoUser.MongoUser;
import com.cscc01.demo.Models.MongoUser.MongoUserDetailsService;
import com.cscc01.demo.Models.Repositories.FileRepository;
import com.cscc01.demo.Models.Repositories.GroupRepository;
import com.cscc01.demo.Models.SchemaBeans.File;
import com.cscc01.demo.Models.SchemaBeans.Group;
import com.cscc01.demo.Models.SchemaBeans.User;

import org.apache.lucene.search.BooleanClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Controller
public class AdvancedSearchController {
	@Autowired
	FileRepository fileRepository;

	public Set<String> getAllTag() {

    	Set<String> tags = new HashSet<>();

    	ArrayList<File> files = (ArrayList<File>) fileRepository.findAll();

    	for (File file: files) {
    		for (String tag: file.getTags()) {
    			tags.add(tag);
    		}
    	}

    	return tags;
    }
    @GetMapping("/advSearch")
    public ModelAndView getAdvSearch() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	MongoUser mongoUser = (MongoUser) auth.getPrincipal();
    	ModelAndView modelAndView = new ModelAndView();
   	 	modelAndView.setViewName("public/advsearch");
   	 	modelAndView.addObject("groups", getAllUserGroups());
   	 	modelAndView.addObject("tags", getAllTag());
   	 	modelAndView.addObject("type",mongoUser.getType().toLowerCase());
   	 	modelAndView.addObject("filetypes",getAllFileTypes());
        return modelAndView ;
    }

    @PostMapping("/advSearch")
    public String postAdvSearch(
            @RequestParam("tags") String tags,
            @RequestParam("body") String body,
            @RequestParam("owner") String owner,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("fileType") String fileType,
			@RequestParam("groupCodes") String groupCodes,
            @RequestParam("viewType") String viewType,
//            @RequestParam("maxHits") Integer maxHits,
            Model model
    ) throws Exception {

        // Validation
        // If both groupCodes and viewType are empty, then set the viewType to public
        // So, if a user doesnt select anything, he wont be able to access all the files
        if (viewType.equals("") && groupCodes.equals("")) {
            viewType = "private";
        }

    	System.out.println(groupCodes);
        ArrayList<ResultObject> resultObjects = LuceneProcess.fromDocuments(
                LuceneSearch.multiSearch(
                        body,
                        tags,
                        groupCodes,
                        owner,
                        fromDate,
                        toDate,
                        fileType,
                        viewType,
                        Integer.MAX_VALUE, BooleanClause.Occur.MUST
                )
        );

        // Add result objects
        model.addAttribute("resultObjects", resultObjects);

        // Add stats
        model.addAttribute("stats", PublicController.chartInfo(resultObjects));

        return "public/advresults";
    }

    @GetMapping("tags/getalltags")
    @ResponseBody
    public Set<String> getAllTags() throws Exception {

    	Set<String> tags = new HashSet<>();

    	ArrayList<File> files = (ArrayList<File>) fileRepository.findAll();

    	for (File file: files) {

    		for (String tag: file.getTags()) {
    			tags.add(tag);
    		}
    	}

    	return tags;
    }

    @GetMapping("files/getallfiletypes")
    @ResponseBody
    public Set<String> getAllFileTypes() {

	    Set<String> fileTypes = new HashSet<>();

	    ArrayList<File> files = (ArrayList<File>) fileRepository.findAll();

	    for (File file: files) {
	        fileTypes.add(file.getContentType());
        }

        return fileTypes;
    }

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    private MongoUserDetailsService userService;

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
}
