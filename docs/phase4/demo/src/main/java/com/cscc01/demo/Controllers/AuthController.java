package com.cscc01.demo.Controllers;

import com.cscc01.demo.Models.SchemaBeans.File;
import com.cscc01.demo.Models.SchemaBeans.Group;
import com.cscc01.demo.Models.SchemaBeans.User;
import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;
import com.cscc01.demo.Models.MongoUser.MongoUser;
import com.cscc01.demo.Models.MongoUser.MongoUserDetailsService;
import com.cscc01.demo.Models.Repositories.FileRepository;
import com.cscc01.demo.Models.Repositories.GroupRepository;
import com.cscc01.demo.Models.Repositories.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;


@Controller
public class AuthController {


    @Autowired
    private MongoUserDetailsService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FileRepository fileRepository;
    
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("public/signup");
        return modelAndView;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("public/signup");
        return modelAndView;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        User userExists2 = userRepository.findUserByUsername(user.getUsername());
        if (userExists !=null) {
        	modelAndView.addObject("dangerMessage", "There is already a user registered with the email provided");
            modelAndView.setViewName("public/signup");
            return modelAndView;
        }
        if (userExists2 !=null) {
        	modelAndView.addObject("dangerMessage", "There is already a user registered with the username provided");
            modelAndView.setViewName("public/signup");
        } else {
            userService.saveUser(user);
            String message = "https://uoftsearch.com/verify/"+user.getId();
            send(user.getEmail(),user.getId(),message);
            modelAndView.addObject("successMessage", "User has been registered successfully. Please check your email to verify your account.");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("public/signup");

        }
        return modelAndView;
    }
    @Autowired
    private UserRepository userRepository;
    
	@RequestMapping(value ="/verify/{id}")
	public ModelAndView recieveVerify(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserById(id);
		if(userExists!=null) {
			userExists.setEnabled(true);
			userRepository.save(userExists);
		}
		modelAndView.addObject("successMessage", "User account is activated. You may now login.");
		modelAndView.setViewName("public/signup");
		return modelAndView;
	}
	@GetMapping(value  = "/resetpasswordemail")
	public String resetPasswordGet() {
		return("public/reset");
	}
	// send email for password reset 
	// need to setup forgot password button.
	@PostMapping(value  = "/resetpasswordemail")
    public void resetPassword(@Valid User user, HttpServletResponse response) throws Exception {
		User userValid = userService.findUserByEmail(user.getEmail());
		if(userValid!=null) {
			String email = userValid.getEmail();
			String id = userValid.getId();
			userValid.setEnabled(false);
			userRepository.save(userValid);
			String message = "use this link to reset your password. https://uoftsearch.com/resetpassword/" + id;
			send(email,id,message);
		}
		response.sendRedirect("/");
	}
	
	// when someone opens the link in the email
	@RequestMapping(value = "/resetpassword/{id}", method = RequestMethod.GET)
	public ModelAndView passwordresetget (@PathVariable("id") String id, HttpServletResponse response) {
		User userExists = userService.findUserById(id);
		ModelAndView modelAndView = new ModelAndView();
		if(userExists!=null && !userExists.isEnabled()) {
			modelAndView.setViewName("public/resetPassword");
	        return modelAndView;
		} 
		modelAndView.setViewName("public/signup");
		return modelAndView;
	}
	
	
	// when the button to reset is clicked
	@RequestMapping(value = "/resetpassword/{id}", method = RequestMethod.POST)
	public ModelAndView passwordresetpost (@Valid User user, @PathVariable("id") String id, Errors errors, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserById(id);
		if(userExists!=null && !userExists.isEnabled()); {
			userExists.setEnabled(true);
			userExists.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			userRepository.save(userExists);
		}
		modelAndView.addObject("successMessage", "Account password was changed.");
		modelAndView.setViewName("public/signup");
		return modelAndView;
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile(String message) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		MongoUser currentUser = (MongoUser)auth.getPrincipal();
		User userLoggedin = userService.findUserById(currentUser.getId());
		ModelAndView modelAndView = new ModelAndView();
		List<String> groups = getAllUserGroups();
		modelAndView.addObject("groups", groups.toArray());
		modelAndView.addObject("user", currentUser);
        modelAndView.setViewName("public/profile");
        //add all plus inst
        if(currentUser.getType().equals("Instructor")) {
        	modelAndView.addObject("instgroups", groupRepository.findAll());
        } else {
        	modelAndView.addObject("instgroups", groupRepository.findAllByType("other"));
        }
        modelAndView.addObject("allUsers", userRepository.findAll());
        modelAndView.addObject("files", getAllFilesForUser(userLoggedin.getFileIds()));
        modelAndView.addObject("successMessage", message);
        return modelAndView;
    }
	
	@RequestMapping(value = "/group", method = RequestMethod.GET)
    public ModelAndView groupView(@RequestParam("groupCode") String groupCode, String message) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		MongoUser currentUser = (MongoUser)auth.getPrincipal();
		List<String> groups = getAllUserGroups();
		ModelAndView modelAndView = new ModelAndView();
		if(groups.contains(groupCode)) {
			Group group = groupRepository.findByCode(groupCode);
			modelAndView.addObject("groupinfo", group);
			modelAndView.addObject("files",getAllFilesForGroup(group.getFileIds()));
			modelAndView.addObject("admins", getAllUsersForGroup(group.getAdminsIds()));
			modelAndView.addObject("users", getAllUsersForGroup(group.getUserIds()));
			modelAndView.addObject("user", currentUser);
	        modelAndView.setViewName("public/group");
	        modelAndView.addObject("allUsers", userRepository.findAll());
	        modelAndView.addObject("successMessage", message);
	        return modelAndView;
		} else {
			message = "Invalid permissions to access group!";
			modelAndView.addObject("successMessage", message);
			modelAndView.setViewName("public/groupinvalid");
			return modelAndView;
		}
		
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
    
    public List<File> getAllFilesForGroup(Set<String> fileIds) {
    	List<File> returnSet = new ArrayList<File>();
    	Iterator<String> files = fileIds.iterator();
    	while(files.hasNext()) {
    		File file = fileRepository.findFileById(files.next());
    		returnSet.add(file);
    	}
		return returnSet;
    	
    }
	
    public List<File> getAllFilesForUser(Set<String> fileIds) {
    	List<File> returnSet = new ArrayList<File>();
    	Iterator<String> files = fileIds.iterator();
    	while(files.hasNext()) {
    		File file = fileRepository.findFileById(files.next());
    		returnSet.add(file);
    	}
		return returnSet;
    	
    }
    public List<User> getAllUsersForGroup(Set<String> userids) {
    	List<User> returnSet = new ArrayList<User>();
    	Iterator<String> users = userids.iterator();
    	while(users.hasNext()) {
    		User user = userRepository.findUserById(users.next());
    		returnSet.add(user);
    	}
		return returnSet;
    	
    }
    
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
    public ModelAndView profileedit(@Valid User user, Errors errors, HttpServletResponse response) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		MongoUser currentUser = (MongoUser)auth.getPrincipal();
		if(currentUser.isEnabled()) {
			User userExists = userService.findUserById(currentUser.getId());
			userExists.setFullname(user.getFullname());
			userExists.setFirstName(user.getFirstName());
			userExists.setLastName(user.getLastName());
			userExists.setUsername(user.getUsername());
//			userExists.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			userRepository.save(userExists);
		}
        return profile("Profile info was changed.");
    }
	
	@RequestMapping(value="/addGroup", method = RequestMethod.POST)
    public ModelAndView addGroup(@RequestParam("groupCode") String groupCode) throws Exception {
    	
        // Get the current User object
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MongoUser currentUser = (MongoUser) auth.getPrincipal();
        String userId = currentUser.getId();

        User user = userRepository.findUserById(userId);

        // Initialize group
        Group group;

        // Check if the group already exists
        Group currGroup = getDefaultGroup(groupCode);

        if (currGroup == null) {
            // Create a new Group
            group = new Group();

            // Set the group code
            group.setCode(groupCode);

            // Set the group type
            group.setType("other");
            
         // Set the group name
            group.setName(groupCode);
        } else if (currGroup.getType()=="default"){
        	// Check if the user is an instructor
            if (!user.getType().equals("Instructor")) {
            	profile("Only instructors can enroll into this group!");
            }
            group = currGroup;
        }
        else {
            group = currGroup;
        }


        // Initialize the users in the group if empty
        Set<String> currUsers = group.getUserIds();
        currUsers = currUsers == null ? new HashSet<>() : currUsers;
        group.setUserIds(currUsers);

        // Add the user as an admin to the group
        Set<String> currAdmins = group.getAdminsIds();
        currAdmins = currAdmins == null ? new HashSet<>() : currAdmins;
        currAdmins.add(user.getId());
        group.setAdminsIds(currAdmins);

        // Set the group files to empty
//        group.setFil(new HashSet<>());

        // Save the group document
        groupRepository.save(group);

        // Add the group to the user's groups set field
        Set<String> groupIds = user.getGroupIds() == null ? new HashSet<>() : user.getGroupIds();
        groupIds.add(group.getId());
        user.setGroupIds(groupIds);

        // Save the user
        userRepository.save(user);
        return profile("Enrolled sucessfully.");
    }

    private Group getDefaultGroup(@RequestParam("groupCode") String groupCode) {
        for (Group repoGroup: groupRepository.findAll()) {
            if (repoGroup.getCode().equals(groupCode)) {
                return repoGroup;
            }
        }
        return null;
    }

    @RequestMapping(value="/groups/addUsers", method = RequestMethod.POST)
    public ModelAndView addUsersToGroup(
            @RequestParam("usersToAdd") List<String> usersToAdd,
            @RequestParam("groupCode") String groupCode
    ) throws Exception {
    	if(usersToAdd.isEmpty()) {
    		return groupView(groupCode,"Please specify users.");
    	}
        addToGroup(usersToAdd, groupCode, "user");
        return groupView(groupCode,"Added users sucessfully.");
    }

    @RequestMapping(value="/groups/addAdmins", method = RequestMethod.POST)
    public ModelAndView addAdminsToGroup(
            @RequestParam("usersToAdd") List<String> usersToAdd,
            @RequestParam("groupCode") String groupCode
    ) throws Exception {
    	if(usersToAdd.isEmpty()) {
    		return groupView(groupCode,"Please specify users.");
    	}
        addToGroup(usersToAdd, groupCode, "admin");
		return groupView(groupCode,"Added users sucessfully.");
    }

    private void addToGroup(List<String> usersToAdd, String groupCode, String accessLevel) throws Exception {
        // Auth boiler plate
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MongoUser currentMongoUser = (MongoUser) auth.getPrincipal();
        String currentUserId = currentMongoUser.getId();
        User currentUser = userRepository.findUserById(currentUserId);


        // Find the Group
        Group group = groupRepository.findByCode(groupCode);
        // Throw exception if it is null
        if (group == null) {
            throw new Exception("Group not found");
        }

        // Check if the currentUser is an admin
        // If not, throw exception
        if (!(group.getAdminsIds().contains(currentUserId))) {
            throw new Exception("User is not an admin");
        }

        // Iterate through all the usersToAdd and add them
        for (String userToAdd: usersToAdd) {

            // Find the other User
            User otherUser = userRepository.findByEmail(userToAdd);
            // Skip if the otherUser is not found
            if (otherUser == null) {
                continue;
            }

            // If we are adding otherUser as a user
            if (accessLevel.equals("user")) {

                // Add other User as a user
                Set<String> groupUsers = group.getUserIds();
                groupUsers.add(otherUser.getId());
                group.setUserIds(groupUsers);

                // Add the Group in the other User
                Set<String> otherUserGroups = otherUser.getGroupIds();
                otherUserGroups.add(group.getId());
                otherUser.setGroupIds(otherUserGroups);
            }
            // If we are adding other user as an admin
            else if (accessLevel.equals("admin")) {

                // Check if the otherUser is a user
                if (!(group.getUserIds().contains(otherUser.getId()))) {
                    continue;
                }

                // Add the otherUser as an admin
                Set<String> groupAdmins = group.getAdminsIds();
                groupAdmins.add(otherUser.getId());
                group.setAdminsIds(groupAdmins);

                // Remove the otherUser as a user
                Set<String> groupUsers = group.getUserIds();
                groupUsers.remove(otherUser.getId());
                group.setUserIds(groupUsers);
            }

            // Save the group
            groupRepository.save(group);

            // Save the other user
            userRepository.save(otherUser);
        }
    }
	
	String send(String email, String id, String message) {
	        try {
	            sendUsingSpark(email,id,message);
	            return "Email Sent!";
	        }catch(Exception ex) {
	            return "Error in sending email: "+ex;
	        }
	    }
	    private void sendUsingSpark(String to, String id,String message) throws SparkPostException {
	    	String API_KEY = "bdb9bfd07228cb5dfc3a838fa3f7855a983a4388";
	        Client client = new Client(API_KEY);
	        
	        client.sendMessage(
	                "verify@mail.uoftsearch.com",
	                to,
	                "Verify UofTsearch account",
	                "The text part of the email",
	                message);
	    }

}
