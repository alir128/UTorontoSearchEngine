package com.cscc01.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cscc01.demo.Models.Repositories.RoleRepository;
import com.cscc01.demo.Models.SchemaBeans.Role;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        // setup
        Files.createDirectories(Paths.get(Constants.PATH));

        SpringApplication.run(DemoApplication.class, args);
    }
    
    @Bean
	CommandLineRunner init(RoleRepository groupRepository) {

	    return args -> {

	        Role adminRole = groupRepository.findByRole("ADMIN");
	        if (adminRole == null) {
	            Role newAdminRole = new Role();
	            newAdminRole.setRole("ADMIN");
	            groupRepository.save(newAdminRole);
	        }

	        Role userGroup = groupRepository.findByRole("USER");
	        if (userGroup == null) {
	            Role newUserRole = new Role();
	            newUserRole.setRole("USER");
	            groupRepository.save(newUserRole);
	        }
	    };

	}
}
