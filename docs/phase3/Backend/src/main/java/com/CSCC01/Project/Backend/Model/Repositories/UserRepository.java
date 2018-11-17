package com.CSCC01.Project.Backend.Model.Repositories;

import com.CSCC01.Project.Backend.Model.SchemaBeans.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	
	public User findByEmail(String email);   
}
