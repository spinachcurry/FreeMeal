package com.app.controller;
 
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController;

import com.app.service.DidsService;  

@RestController
@RequestMapping   
public class DidsController {
	
	@Autowired
	private DidsService didsService;  
	
	    // 모든 찜 관련 기능 처리
	@PostMapping("/handleDibs")
    public ResponseEntity<?> handleDibs(@RequestBody Map<String, Object> requestBody) {
          return didsService.handleDibs(requestBody);
    }  
}
