package com.cscc01.demo.Models.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cscc01.demo.Models.SchemaBeans.Group;

public interface GroupRepository extends MongoRepository<Group, String>{
	Group findByName(String name);
	Group findByCode(String code);
	Group findGroupById(String id);
	List<Group> findAllByType(String type);
}
