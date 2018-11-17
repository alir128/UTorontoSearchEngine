package com.CSCC01.Project.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.CSCC01.Project.Backend.Model.SchemaBeans.Group;
import com.CSCC01.Project.Backend.Model.Repositories.GroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) throws Exception {

	    // setup
        Files.createDirectories(Paths.get(Constants.PATH));

		SpringApplication.run(BackendApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(GroupRepository groupRepository) {

	    return args -> {

	        Group adminRole = groupRepository.findByGroup("ADMIN");
	        if (adminRole == null) {
	            Group newAdminRole = new Group();
	            newAdminRole.setGroup("ADMIN");
	            groupRepository.save(newAdminRole);
	        }

	        Group userGroup = groupRepository.findByGroup("USER");
	        if (userGroup == null) {
	            Group newUserRole = new Group();
	            newUserRole.setGroup("USER");
	            groupRepository.save(newUserRole);
	        }
	    };

	}

}
