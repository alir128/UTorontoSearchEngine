package com.cscc01.demo.Controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cscc01.demo.Controllers.PublicOutputBeans.CommentsResponseAjax;
import com.cscc01.demo.Models.MongoUser.MongoUser;
import com.cscc01.demo.Models.Repositories.CommentRepository;
import com.cscc01.demo.Models.SchemaBeans.Comment;
import org.springframework.web.bind.annotation.PathVariable;
import javax.validation.Valid;

@Controller
public class commentsController {
	@Autowired
    private CommentRepository CommentRepository;
	
	@PostMapping(value  = "/save/{fileId}")
    public ResponseEntity<?> postCommentFile(@PathVariable("fileId") String fileId, @RequestBody @Valid Comment comment, Errors errors ) throws Exception {
		
		CommentsResponseAjax response = new CommentsResponseAjax();
    	if(errors.hasErrors()) {
    		response.setMessage(errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
    		return ResponseEntity.badRequest().body(response);
    	}
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	MongoUser username = (MongoUser)auth.getPrincipal();
    	
    	comment.setOwner(username.getFullName());
    	String time = new SimpleDateFormat("HH:mm").format(new Date());
        String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        comment.setTime(time);
        comment.setDate(date);
        comment.setFileId(fileId);
        CommentRepository.save(comment);
        
        List<Comment> comments = CommentRepository.findAllByfileId(fileId);
        response.setMessage("success");
        response.setResult(comments);
        

        return ResponseEntity.ok(response);
    }
	
	@PostMapping(value  = "/view/{fileId}")
    public ResponseEntity<?> viewCommentFile(@PathVariable("fileId") String fileId, @RequestBody @Valid Comment comment, Errors errors ) throws Exception {
		
		CommentsResponseAjax response = new CommentsResponseAjax();
    	if(errors.hasErrors()) {
    		response.setMessage(errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
    		return ResponseEntity.badRequest().body(response);
    	}
        List<Comment> comments = CommentRepository.findAllByfileId(fileId);
        response.setMessage("success");
        response.setResult(comments);
        

        return ResponseEntity.ok(response);
    }
	
	
	@PostMapping(value  = "/saveGroup/{groupCode}")
    public ResponseEntity<?> postCommentGroup(@PathVariable("groupCode") String groupCode, @RequestBody @Valid Comment comment, Errors errors ) throws Exception {
		
		CommentsResponseAjax response = new CommentsResponseAjax();
    	if(errors.hasErrors()) {
    		response.setMessage(errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
    		return ResponseEntity.badRequest().body(response);
    	}
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	MongoUser username = (MongoUser)auth.getPrincipal();
    	
    	comment.setOwner(username.getFullName());
    	String time = new SimpleDateFormat("HH:mm").format(new Date());
        String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        comment.setTime(time);
        comment.setDate(date);
        comment.setGroupCode(groupCode);
        CommentRepository.save(comment);
        
        List<Comment> comments = CommentRepository.findAllByGroupCode(groupCode);
        response.setMessage("success");
        response.setResult(comments);
        

        return ResponseEntity.ok(response);
    }
	
	@PostMapping(value  = "/viewGroup/{groupCode}")
    public ResponseEntity<?> viewCommentGroup(@PathVariable("groupCode") String groupCode, @RequestBody @Valid Comment comment, Errors errors ) throws Exception {
		
		CommentsResponseAjax response = new CommentsResponseAjax();
    	if(errors.hasErrors()) {
    		response.setMessage(errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
    		return ResponseEntity.badRequest().body(response);
    	}
        List<Comment> comments = CommentRepository.findAllByGroupCode(groupCode);
        response.setMessage("success");
        response.setResult(comments);
        

        return ResponseEntity.ok(response);
    }
	
}
