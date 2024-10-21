package com.app.controller;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import com.app.dto.UserDTO;
import com.app.dto.UserResultDTO;
import com.app.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/view")
    public ResponseEntity<?> view(@RequestParam("url") String url) {
        return loginService.viewFile(url);
    }

    @PostMapping("/userAction")
    public ResponseEntity<?> userAction(@RequestBody Map<String, Object> requestBody, HttpServletRequest req) {
        return loginService.processUserAction(requestBody, req);
    } 
      
    @PostMapping("/updateUser")
    public Map<String, Object> updateUser(
            HttpServletRequest req,
            @RequestParam("userId") String userId,
            @RequestParam("user_Nnm") String user_Nnm,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("review") String review,
            @RequestParam("password") String password,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            Model model) {
        return loginService.updateUser(req, userId, user_Nnm, phone, email, review, password, profileImage);
    }
 
}
