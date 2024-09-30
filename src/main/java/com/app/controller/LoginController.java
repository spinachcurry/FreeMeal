package com.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.app.userDTO.UserDTO;
import com.app.userDTO.UserResultDTO;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

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

	        // 사용자 정보를 기반으로 JWT 토큰 생성
	        Map<String, String> paramMap = new HashMap<>();
	        paramMap.put("name", userResultDTO.getUserDTO().getUserId()); // 사용자 아이디를 토큰에 포함

	        String jwtToken = setToken(paramMap);  // JWT 토큰 생성

	        // 세션에 사용자 정보와 JWT 토큰 저장
	        session.setAttribute("userId", userResultDTO.getUserDTO().getUserId());
	        session.setAttribute("user_Nnm", userResultDTO.getUserDTO().getUser_Nnm()); // 닉네임 저장
	        session.setAttribute("phone", userResultDTO.getUserDTO().getPhone());       // 전화번호 저장
	        session.setAttribute("email", userResultDTO.getUserDTO().getEmail());       // 이메일 저장
	        session.setAttribute("password", userResultDTO.getUserDTO().getPassword()); // 비밀번호 저장
	        session.setAttribute("jwtToken", jwtToken);  // 세션에 JWT 토큰 저장
	        
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
        // 세션에서 사용자 정보와 JWT 토큰을 가져옴
        HttpSession session = req.getSession(false);  // 세션이 없으면 null을 반환
        String user_Nnm = null;
        String userId = null;
        String phone = null;
        String password = null;
        String email = null;
        String jwtToken = null;  // JWT 토큰도 가져오기

        if (session != null) {
            userId = (String) session.getAttribute("userId");
            user_Nnm = (String) session.getAttribute("user_Nnm");  // 닉네임 가져오기
            phone = (String) session.getAttribute("phone");        // 전화번호 가져오기
            email = (String) session.getAttribute("email");        // 이메일 가져오기
            password = (String) session.getAttribute("password");  // 비밀번호 가져오기
            jwtToken = (String) session.getAttribute("jwtToken");  // JWT 토큰을 세션에서 가져옴
        }

        // 세션 정보가 있을 경우에만 모델에 추가하여 View로 전달
        model.addAttribute("user_Nnm", user_Nnm);
        model.addAttribute("userId", userId);
        model.addAttribute("phone", phone);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("jwtToken", jwtToken);  // JWT 토큰도 모델에 추가

        log.info(jwtToken);  // 로그로 사용자 정보 출력

        // 로그인 여부와 관계없이 mainDetail 페이지를 반환
        return "html/mainDetail";
    }
    
    @GetMapping("/myPage")
    public String myPage(HttpServletRequest req, Model model) {
        // 세션에서 사용자 정보와 JWT 토큰을 가져옴
        HttpSession session = req.getSession(false);  // 세션이 없으면 null을 반환
        String user_Nnm = null;
        String userId = null;
        String phone = null;
        String password = null;
        String email = null;
        String jwtToken = null;  // JWT 토큰도 가져오기

        if (session != null) {
            userId = (String) session.getAttribute("userId");
            user_Nnm = (String) session.getAttribute("user_Nnm");  // 닉네임 가져오기
            phone = (String) session.getAttribute("phone");        // 전화번호 가져오기
            email = (String) session.getAttribute("email");        // 이메일 가져오기
            password = (String) session.getAttribute("password");  // 비밀번호 가져오기
            jwtToken = (String) session.getAttribute("jwtToken");  // JWT 토큰을 세션에서 가져옴
        }

        // 세션 정보가 있을 경우에만 모델에 추가하여 View로 전달
        model.addAttribute("user_Nnm", user_Nnm);
        model.addAttribute("userId", userId);
        model.addAttribute("phone", phone);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("jwtToken", jwtToken);  // JWT 토큰도 모델에 추가

        log.info(jwtToken);  // 로그로 사용자 정보 출력

        return "html/myPage";
    }
    
    
    
    //토큰용 
    private String setToken(Map<String, String> paramMap) {
		// 키 생성
		String strKey = "c2hlbGxmb2xkZXIxMjM0NTY3ODlEZXZKV1QxMjM0NTY3ODk=";
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(strKey));
//		SecretKey key = Keys.hmacShaKeyFor("shellfolder123456789DevJWT123456789".getBytes());
		
		// 유효 날짜 생성
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MINUTE, 10);
		
		// Token 해더 정보 생성
		Map<String, String> header = new HashMap<>();
		header.put("alg", "HS256");
		header.put("typ", "JWT");
		
		// Token Payload 영역에 사용자 이름 생성
		Map<String, String> userMap = new HashMap<>();
		userMap.put("name", paramMap.get("name"));
		
		// Token 발행
		 return Jwts.builder()
		            .setHeader(header) // 헤더 설정
		            .setClaims(userMap) // 페이로드 설정
		            .setIssuer("BackProject") // 발행자 설정
		            .setSubject("UserInfo") // 주제 설정
		            .setExpiration(date.getTime()) // 만료 시간 설정
		            .setIssuedAt(Calendar.getInstance().getTime()) // 발행 시간 설정
		            .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 설정
		            .compact(); // JWT 생성 및 반환
	}
    
    
}