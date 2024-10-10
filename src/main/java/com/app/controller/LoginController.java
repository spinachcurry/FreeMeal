package com.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.app.userDTO.ReviewDTO;
import com.app.userDTO.UserDTO;
import com.app.userDTO.UserResultDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.io.Decoders; 
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import com.app.Utill.JWTUtility;
import com.app.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@RequestMapping
@Slf4j
@CrossOrigin(origins = "http://localhost:3001")// React 클라이언트 주소 
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	 // 파일 저장 경로를 application.properties에서 불러오기
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @GetMapping("/view")
    public ResponseEntity<?> view(@RequestParam("url") String url) {
		try {
		      String path = uploadDir.concat("/").concat(url);
		      File file = new File(path);
		      return ResponseEntity.ok()
		        .contentLength(file.length())
		        .contentType(MediaType.parseMediaType("image/png"))
		        .body(new InputStreamResource(new FileInputStream(file)));
		    } catch (Exception e) {
		      e.printStackTrace();
		      return ResponseEntity.notFound().build();
		    }
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
        String profileImageUrl = null; 
        String jwtToken = null;  // JWT 토큰도 가져오기review

        if (session != null) {
            userId = (String) session.getAttribute("userId");
            user_Nnm = (String) session.getAttribute("user_Nnm");  // 닉네임 가져오기
            phone = (String) session.getAttribute("phone");        // 전화번호 가져오기
            email = (String) session.getAttribute("email");        // 이메일 가져오기
            password = (String) session.getAttribute("password");  // 비밀번호 가져오기
            profileImageUrl = (String) session.getAttribute("profileImageUrl"); 
            jwtToken = (String) session.getAttribute("jwtToken");  // JWT 토큰을 세션에서 가져옴
        }

        // 세션 정보가 있을 경우에만 모델에 추가하여 View로 전달
        model.addAttribute("user_Nnm", user_Nnm);
        model.addAttribute("userId", userId);
        model.addAttribute("phone", phone);
        model.addAttribute("email", email); 
        model.addAttribute("password", password);
        model.addAttribute("profileImageUrl", profileImageUrl);
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
        String profileImageUrl = null;
        String jwtToken = null;  // JWT 토큰도 가져오기

        if (session != null) {
            userId = (String) session.getAttribute("userId");
            user_Nnm = (String) session.getAttribute("user_Nnm");  // 닉네임 가져오기
            phone = (String) session.getAttribute("phone");        // 전화번호 가져오기
            email = (String) session.getAttribute("email");        // 이메일 가져오기
            password = (String) session.getAttribute("password");  // 비밀번호 가져오기
            profileImageUrl = (String) session.getAttribute("profileImageUrl");  // 비밀번호 가져오기
            jwtToken = (String) session.getAttribute("jwtToken");  // JWT 토큰을 세션에서 가져옴
        }

        // 세션 정보가 있을 경우에만 모델에 추가하여 View로 전달
        model.addAttribute("user_Nnm", user_Nnm);
        model.addAttribute("userId", userId);
        model.addAttribute("phone", phone);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("profileImageUrl", profileImageUrl);
        model.addAttribute("jwtToken", jwtToken);  // JWT 토큰도 모델에 추가

        log.info(jwtToken);  // 로그로 사용자 정보 출력
    	return "html/myPage";
    } 
    
    @GetMapping("login")
	public String login() { 
		return "html/login";
	}
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        UserResultDTO userResultDTO = loginService.findByUser(userDTO);

        if (userResultDTO.isStatus()) { // 로그인 성공 시
            String jwtToken = setToken(Collections.singletonMap("name", userDTO.getUserId()));
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", true);
            response.put("user", userResultDTO.getUserDTO());
            response.put("jwtToken", jwtToken);
            System.out.println("Profile Image URL: " + userResultDTO.getUserDTO().getProfileImageUrl());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 틀렸습니다.");
        }
    }
 
	 @GetMapping("signup")
	    public String signup(HttpServletRequest req) {   
	        req.getSession().removeAttribute("duplicateError"); 
	        req.getSession().removeAttribute("errorMessage");
	    	return "html/signup";
	    }
	    
	 @PostMapping("/signup")
	 @ResponseBody
	 public ResponseEntity<?> signup(@RequestBody UserDTO userDTO, HttpServletRequest req) {
	     // UserResultDTO를 사용하여 가입 결과를 저장
	     UserResultDTO userResultDTO = loginService.signup(userDTO);

	     // 가입 성공 여부에 따른 처리
	     if (userResultDTO.isStatus()) {
	         // 성공 메시지를 반환
	         return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
	     } else {
	         // 중복된 아이디일 경우
	         if ("중복된 아이디가 있습니다.".equals(userResultDTO.getMessage())) {
	             return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", "중복된 아이디가 있습니다."));
	         }

	         // 기타 실패 시에도 실패 메시지를 반환
	         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", userResultDTO.getMessage()));
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
	     
    @GetMapping("/updateUserInfo")
    public String updateUserInfo(HttpServletRequest req, Model model) {
        // 세션에서 사용자 정보와 JWT 토큰을 가져옴
        HttpSession session = req.getSession(false);  // 세션이 없으면 null을 반환
        String user_Nnm = null;
        String userId = null;
        String phone = null;
        String password = null; 
        String profileImageUrl = null; 
        String email = null;
        String review = null;
        String jwtToken = null;  // JWT 토큰도 가져오기 review

        if (session != null) {
            userId = (String) session.getAttribute("userId");
            user_Nnm = (String) session.getAttribute("user_Nnm");  // 닉네임 가져오기
            phone = (String) session.getAttribute("phone");        // 전화번호 가져오기
            email = (String) session.getAttribute("email");        // 이메일 가져오기
            review = (String) session.getAttribute("review");        // 한마디 가져오기
            password = (String) session.getAttribute("password");  // 비밀번호 가져오기
            profileImageUrl = (String) session.getAttribute("profileImageUrl");  // 비밀번호 가져오기
            jwtToken = (String) session.getAttribute("jwtToken");  // JWT 토큰을 세션에서 가져옴
        }

        // 세션 정보가 있을 경우에만 모델에 추가하여 View로 전달
        model.addAttribute("user_Nnm", user_Nnm);
        model.addAttribute("userId", userId);
        model.addAttribute("phone", phone);
        model.addAttribute("email", email);
        model.addAttribute("review", review);
        model.addAttribute("password", password);
        model.addAttribute("profileImageUrl", profileImageUrl);
        model.addAttribute("jwtToken", jwtToken);  // JWT 토큰도 모델에 추가 
        log.info(jwtToken);  // 로그로 사용자 정보 출력

        return "html/updateUserInfo";
    }
     
    @ResponseBody
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
    	Map<String, Object> resultMap = new HashMap<>();
    	boolean check = true;
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        String profileImageUrl = null;

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    System.out.println("Directory does not exist. Creating directory...");
                    directory.mkdirs();
                }

                String fileExtension = getFileExtension(profileImage.getOriginalFilename());
                String fileName = UUID.randomUUID().toString() + "." + fileExtension;

                System.out.println("Uploading file: " + fileName);

                File destinationFile = new File(directory, fileName);
                profileImage.transferTo(destinationFile);

                profileImageUrl = fileName;
                System.out.println("File successfully uploaded to: " + profileImageUrl);

            } catch (IOException e) {
                e.printStackTrace();
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드에 실패했습니다: " + e.getMessage());
                resultMap.put("msg", "파일 업로드에 실패했습니다: " + e.getMessage());
                check = false;
            }
        } 

        resultMap.put("state", check);
        
        if(check) {
	        // UserDTO 생성 및 업데이트 호출
	        UserDTO userDTO = new UserDTO();
	        userDTO.setUserId(userId);
	        userDTO.setUser_Nnm(user_Nnm);
	        userDTO.setPhone(phone); 
	        userDTO.setEmail(email);
	        userDTO.setReview(review);
	        userDTO.setPassword(password);
	        userDTO.setProfileImageUrl(profileImageUrl);
	
	        // 서비스 단 호출
	        loginService.updateUser(userDTO);
	        System.out.println("Profile update successful.");
	
	        // 프로필 업데이트가 성공적으로 완료된 경우
	        resultMap = getUserData(req);
        }
                
        return resultMap;
    }

    private String getFileExtension(String originalFilename) {
        // 파일 확장자 추출 로직 수정
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        } else {
            return ""; // 확장자 없음
        }
    }
 
    public Map<String, Object> getUserData(HttpServletRequest request) {
    	Map<String, Object> response = new HashMap<>();
    	response.put("status", false);
    	// 세션에서 userId를 가져옴
    	String token = request.getHeader("Authorization");
    	System.out.println("Token" + token);
    	if(token != null && isValidToken(token)) {
    		
	        String userId = getUser(token);
	        // 세션에 userId가 없을 경우, 로그인되지 않은 상태로 처리
	        if (userId == null) { 
	        	response.put("status", false);
	        } else {
		        // UserDTO 객체를 생성하고 userId를 설정
		        UserDTO userDTO = new UserDTO();
		        userDTO.setUserId(userId);
		        // 사용자 정보 조회
		        UserResultDTO userResultDTO = loginService.findOne(userDTO);
		        // 조회된 사용자 정보 반환 또는 404 Not Found
		        if (userResultDTO.isStatus()) {
		            response.put("status", true);
		            response.put("user", userResultDTO.getUserDTO());
		        } 
	        }

		} else { 
			response.put("msg", "사용자 Token이 만료 되었습니다.");
    	}
        return response;
    }

    //리뷰 불러오기Review
    @GetMapping("/getReviewsByStatus")
    public ResponseEntity<?> getReviewsByStatus(@RequestParam("userId") String userId) {
    	System.out.println("==========================="+userId);
        try {
            List<ReviewDTO> reviews = loginService.getReviewsByStatus(userId);
            if (reviews == null || reviews.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰가 없습니다.");
            }
            return ResponseEntity.ok(reviews);
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // 예외 로그 출력
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("리뷰를 가져오는 중 서버 오류가 발생했습니다.");
        }
    }
    // 리뷰 수정 요청 처리
    @PostMapping("/updateReview")
    public ResponseEntity<?> updateReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            // 리뷰 업데이트 수행
            boolean isUpdated = loginService.updateReview(reviewDTO);

            if (isUpdated) {
                // 업데이트 성공
                return ResponseEntity.ok().body("리뷰가 성공적으로 수정되었습니다.");
            } else {
                // 업데이트 실패 (예: 해당 리뷰가 존재하지 않는 경우)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            // 예외 처리 및 오류 응답
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("리뷰를 수정하는 중 오류가 발생했습니다.");
        }
    }


  //가게상세 리뷰 불러오기
    @GetMapping("/reviews")
    public ResponseEntity<?> getStoreReviews() {
        try {
            List<ReviewDTO> storeReviews = loginService.findStoreReviews();
 
            System.out.println("StoreReviews: " + storeReviews);

            if (storeReviews == null || storeReviews.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰가 없습니다.");
            }
            return ResponseEntity.ok(storeReviews);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("리뷰를 가져오는 중 서버 오류가 발생했습니다.");
        }
    }
    
    
	//토큰용 
    private String setToken(Map<String, String> paramMap) {
		  
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
		            .signWith(getKey(), SignatureAlgorithm.HS256) // 서명 알고리즘 설정
		            .compact(); // JWT 생성 및 반환
	}
    
    private Claims getToken(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
    private String getUser(String token) {
		return getToken(token).get("name", String.class);
	}
	
	private SecretKey getKey() {
		// 키 생성
		String strKey = "c2hlbGxmb2xkZXIxMjM0NTY3ODlEZXZKV1QxMjM0NTY3ODk=";
		return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(strKey));
	}
	
	public boolean isValidToken(String token) {
	    try {
	        Claims claims = getToken(token);
	        log.info("===============================================");
	        log.info("|ExpireTime\t: {}|", claims.getExpiration());
	        log.info("|IIssuedTime\t: {}|", claims.getIssuedAt());
	        log.info("|RealTime\t: {}|", Calendar.getInstance().getTime());
	        log.info("===============================================");
	        return true;
	    } catch (ExpiredJwtException exception) {
	    	log.info("==============");
	    	log.error("Token Expired");
	    } catch (JwtException exception) {
	    	log.info("==============");
	    	log.error("Token Tampered");
	    } catch (NullPointerException exception) {
	    	log.info("==============");
	    	log.error("Token is null");
	    }
	    log.info("==============");
	    return false;
	}
    
}