package com.app.controller;

import org.springframework.web.bind.annotation.RequestMapping; 

import com.app.userDTO.UserDTO;
import com.app.userDTO.UserResultDTO; 
import com.app.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping
@Slf4j
public class LoginController {
	
	@Autowired
	private LoginService loginService;

	@GetMapping("login")
	public String login() {
		log.info("---- 테스트용 ---");
		return "html/login";
	}
    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpServletRequest req) {
        UserResultDTO userResultDTO = loginService.findByUser(userDTO);  // loginService로 수정 
        log.info("---- 테스트용1 ---"); 
        if (userResultDTO.isStatus()) {
            HttpSession http = req.getSession();
            http.setAttribute("userId", userResultDTO.getUserDTO().getUserId());
            http.setAttribute("password", userResultDTO.getUserDTO().getPassword());
            //http.setAttribute("userRoles", userResultDTO.getUserDTO().getUserRoles());
            
            log.info("로그인 서비스 호출 완료, 반환 값: " + userResultDTO);
            log.info("아이디/비번 :" + userResultDTO.getUserDTO().getUserId() + "/" + userResultDTO.getUserDTO().getPassword());
            
            return "html/mainDetail";
        }
        
        log.info("로그인 상태 확인: " + userResultDTO.isStatus());
        if (userResultDTO.isStatus()) {
            // 세션 설정 및 리다이렉트 처리
        } else {
            log.info("로그인 실패: 아이디 또는 비밀번호가 틀렸습니다.");
        }
        
        return "html/mainDetail";
    }

    
    @GetMapping("signup")
    public String signup() { 
    	log.info("회원가입 페이지");
    	return "html/signup";
    }
    @PostMapping("/signup")
    public String signup(@ModelAttribute UserDTO userDTO, HttpServletRequest req) {
    	UserResultDTO userResultDTO = loginService.signup(userDTO); // signup 메서드 호출
    	log.info("0925_데이터 안넘어간다. 여기서부터해라.");
    	// 가입 성공 여부 확인
    	if (userResultDTO.isStatus()) {
    		// 성공 메시지를 세션에 저장 (optional)
    		req.getSession().setAttribute("successMessage", userResultDTO.getMessage());
    		log.info("회원가입 여부 알림-------------성공---------------");
    		return "html/mainDetail"; // 성공 시 메인 화면으로 리다이렉션
    	} else {
    		// 실패 메시지를 세션에 저장 (optional)
    		req.getSession().setAttribute("errorMessage", userResultDTO.getMessage());
    		log.info("회원가입 여부 알림-------------실패---------------");
    		return "html/login"; // 실패 시 회원가입 페이지로 리다이렉션
    	}
    }
    
    @GetMapping("/logout")
	public String logout(HttpServletRequest req) {
		req.getSession().invalidate();
		return "redirect:/";
	}
    
    @GetMapping("mainDetail")
   	public String mainDetail() { 
   		return "html/mainDetail";
   	}
    
    
}