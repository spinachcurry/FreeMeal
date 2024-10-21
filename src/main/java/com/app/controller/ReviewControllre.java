package com.app.controller;
 
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;  
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController;  
import com.app.service.ReviewService; 

@RestController
@RequestMapping 
@CrossOrigin(origins = "http://localhost:3000") // React 앱이 실행되는 포트
public class ReviewControllre {
	
	@Autowired
	private ReviewService reviewService;
	 
	@PostMapping("/reviewAction")
	public ResponseEntity<?> reviewAction(@RequestBody Map<String, Object> requestBody) {
	    return reviewService.handleReviewAction(requestBody);
	}   
} 
