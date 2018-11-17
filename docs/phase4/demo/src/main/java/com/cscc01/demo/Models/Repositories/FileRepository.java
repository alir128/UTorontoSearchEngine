package com.cscc01.demo.Models.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cscc01.demo.Models.SchemaBeans.File;



public interface FileRepository extends MongoRepository<File, String>{
	
	public List<String> findAllByContentType(String ContentType);
	public List<File> findByOwner(String owner);
	File findFileById(String id);
}
