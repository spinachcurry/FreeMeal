package com.app.controller;

import org.springframework.web.bind.annotation.RequestMapping; 
import java.util.UUID; 


import com.app.userDTO.UserDTO;
import com.app.userDTO.UserResultDTO; 
import com.app.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	    UserResultDTO userResultDTO = loginService.findByUser(userDTO);  // 로그인 서비스 호출
	    log.info("---- 로그인 처리 시작 ----");

	    if (userResultDTO.isStatus()) {  // 로그인 성공 시
	        // 세션을 생성하거나 존재하는 세션을 가져옴
	        HttpSession session = req.getSession();

	        // UUID를 이용해 고유한 세션 키 생성
	        String sessionKey = UUID.randomUUID().toString();

	        // 세션에 사용자 정보와 세션 키 저장
	        session.setAttribute("userId", userResultDTO.getUserDTO().getUserId());
	        session.setAttribute("password", userResultDTO.getUserDTO().getPassword());
	        session.setAttribute("sessionKey", sessionKey);  // 세션 키 저장

	        log.info("로그인 성공! 세션 키: " + sessionKey);
	        log.info("아이디/비번: " + userResultDTO.getUserDTO().getUserId() + "/" + userResultDTO.getUserDTO().getPassword());

	        return "redirect:/mainDetail";  // 로그인 성공 후 메인 페이지로 리다이렉트
	    } else {
	        // 로그인 실패 시
	        log.info("로그인 실패: 아이디 또는 비밀번호가 틀렸습니다.");
	        req.getSession().setAttribute("loginError", "아이디 또는 비밀번호가 틀렸습니다.");
	        return "html/login";  // 실패 시 로그인 페이지로 돌아감
	    }
	}
    
    @GetMapping("signup")
    public String signup(HttpServletRequest req) {  
    	log.info("테스트--회원가입");
        req.getSession().removeAttribute("duplicateError");
    	log.info("테스트--회원가입---오류찾기------------------------------------------");
        req.getSession().removeAttribute("errorMessage");
    	return "html/signup";
    }
    
    @PostMapping("/signup")
    public String signup(@ModelAttribute UserDTO userDTO, HttpServletRequest req, Model model) {
        UserResultDTO userResultDTO = loginService.signup(userDTO); // signup 메서드 호출

        log.info("UserDTO received: {}", userDTO.toString());
        log.info("UserResultDTO 상태: isStatus={}, message={}", userResultDTO.isStatus(), userResultDTO.getMessage());

        // 가입 성공 여부 확인
        if (userResultDTO.isStatus()) {
            req.getSession().setAttribute("successMessage", userResultDTO.getMessage());
            req.getSession().removeAttribute("duplicateError"); // 성공 시 중복 오류 메시지 제거
            log.info("회원가입 여부 알림-------------성공---------------");
            return "html/mainDetail"; // 성공 시 메인 화면으로 리다이렉션
        } else {
            req.getSession().setAttribute("errorMessage", userResultDTO.getMessage());
            log.info("회원가입 여부 알림-------------실패---------------");

            // 중복된 아이디일 경우
            if ("중복된 아이디가 있습니다.".equals(userResultDTO.getMessage())) {
                req.getSession().setAttribute("duplicateError", "중복된 아이디가 있습니다."); // 중복 메시지 세션에 저장
                model.addAttribute("userDTO", userDTO); // 폼에 입력했던 데이터 유지
                return "html/signup";  // 회원가입 페이지로 이동
            }

            model.addAttribute("userDTO", userDTO); // 기타 실패 시에도 입력 데이터 유지
            return "html/login"; // 기타 실패 시 로그인 페이지로 이동
        }
    }
    
    @PostMapping("/logout")
    public String logout(HttpServletRequest req) {
        // 세션 무효화 (세션 내 모든 데이터를 삭제)
        HttpSession session = req.getSession();
        session.invalidate();

        log.info("로그아웃 완료, 세션 무효화됨.");
        return "redirect:mainDetail";  // 로그인 페이지로 리다이렉트
    }
    
    @GetMapping("/mainDetail")
    public String mainDetail(HttpServletRequest req, Model model) {
        // 세션에서 사용자 정보와 세션 키를 가져오기
        HttpSession session = req.getSession(false);  // 세션이 없을 때 새로 만들지 않음
        String userId = null;
        String sessionKey = null;

        if (session != null) {
            userId = (String) session.getAttribute("userId");
            sessionKey = (String) session.getAttribute("sessionKey");
        }

        // 세션 정보가 있을 경우에만 모델에 추가하여 View로 전달
        model.addAttribute("userId", userId);
        model.addAttribute("sessionKey", sessionKey);

        // 로그인 여부와 관계없이 mainDetail 페이지를 반환
        return "html/mainDetail";
    }
    
}