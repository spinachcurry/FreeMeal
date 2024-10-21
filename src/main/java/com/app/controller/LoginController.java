package com.app.controller;

import org.springframework.web.multipart.MultipartFile;
 
import com.app.dto.UserDTO;
import com.app.dto.UserResultDTO;
import com.app.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;  

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.ResponseEntity; 

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader; 
 
  @RestController 
public class LoginController {

    @Autowired
    private LoginService loginService;
    
    @GetMapping("/view")
    public ResponseEntity<?> view(@RequestParam("url") String url) {
        return loginService.getFileAsResponse(url);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(loginService.login(userDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(loginService.signup(userDTO));
    }
 
    @PostMapping("/updateUser")
    public ResponseEntity<?> updateUser(
            HttpServletRequest req,
            @RequestParam("userId") String userId,
            @RequestParam("user_Nnm") String user_Nnm,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("review") String review,
            @RequestParam("password") String password,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("status") String status) {  // status 추가
        return ResponseEntity.ok(loginService.updateUserProfile(req, userId, user_Nnm, phone, email, review, password, profileImage, status));
    } 
}
