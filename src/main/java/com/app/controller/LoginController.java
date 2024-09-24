package com.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.userDTO.UserDTO;
import com.app.userDTO.UserResultDTO;
import com.app.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping
public class LoginController {
	
	private LoginService loginService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/main")
	public String main() {
		return "main";
	}	
    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpServletRequest req) {
        UserResultDTO userResultDTO = loginService.findByUser(userDTO);  // loginService로 수정
        if (userResultDTO.isStatus()) {
            HttpSession http = req.getSession();
            http.setAttribute("userID", userResultDTO.getUserDTO().getUserId());
            http.setAttribute("userPW", userResultDTO.getUserDTO().getPassword());
            http.setAttribute("userRoles", userResultDTO.getUserDTO().getUserRoles());
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
	public String logout(HttpServletRequest req) {
		req.getSession().invalidate();
		return "redirect:/";
	}
    
}