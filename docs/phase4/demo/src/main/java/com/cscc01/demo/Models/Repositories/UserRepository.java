package com.cscc01.demo.Models.Repositories;

import com.cscc01.demo.Models.SchemaBeans.User;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

	public User findByEmail(String email);
	public User findUserById(String id);
	public User findUserByUsername(String username);
}
