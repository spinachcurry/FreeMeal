package com.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.userDTO.UserDTO;
import com.app.userDTO.UserResultDTO; 
import com.app.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping
@Slf4j
public class LoginController {
	
	private LoginService loginService;

	@GetMapping("login")
	public String login() {
		log.info("테스트용---");
		
		return "login";
	}
	
    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpServletRequest req) {
        UserResultDTO userResultDTO = loginService.findByUser(userDTO);  // loginService로 수정
        if (userResultDTO.isStatus()) {
            HttpSession http = req.getSession();
            http.setAttribute("userID", userResultDTO.getUserDTO().getUserId());
            http.setAttribute("userPW", userResultDTO.getUserDTO().getPassword());
            //http.setAttribute("userRoles", userResultDTO.getUserDTO().getUserRoles());
            log.info("아이디/비번 -----{}-----" + "userID" + "userPW");
            return "main";
            //return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
	public String logout(HttpServletRequest req) {
		req.getSession().invalidate();
		return "redirect:/";
	}
    
}