package com.cscc01.demo.Models.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cscc01.demo.Models.SchemaBeans.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
	
	public List<Comment> findAllByfileId(String fileId);
	public List<Comment> findAllByGroupCode(String groupCode);
}
