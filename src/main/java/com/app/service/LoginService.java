package com.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.UserDTO;
import com.app.dto.UserResultDTO;
import com.app.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest; 
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService {
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${file.upload-dir}")
    private String uploadDir;
 
	   public ResponseEntity<?> processUserAction(Map<String, Object> requestBody, HttpServletRequest req) {
	        String action = (String) requestBody.get("action");

	        switch (action) {
	            case "login":
	                return handleLogin(requestBody);

	            case "signup":
	                return handleSignup(requestBody);

	            case "updateUser":
	                return handleUpdateUser(requestBody, req);

	            default:
	                return ResponseEntity.badRequest().body("잘못된 요청입니다.");
	        }
	    }
	   private ResponseEntity<?> handleLogin(Map<String, Object> requestBody) {
	        UserDTO userDTO = new UserDTO();
	        userDTO.setUserId((String) requestBody.get("userId"));
	        userDTO.setPassword((String) requestBody.get("password"));
	        return login(userDTO);
	    }

	   private ResponseEntity<?> handleSignup(Map<String, Object> requestBody) {
		    UserDTO userDTO = new UserDTO();
		    userDTO.setUserId((String) requestBody.get("userId"));
		    userDTO.setPassword((String) requestBody.get("password"));
		    userDTO.setEmail((String) requestBody.get("email"));
		    userDTO.setName((String) requestBody.get("name"));
		    userDTO.setUser_Nnm((String) requestBody.get("user_Nnm"));
		    userDTO.setPhone((String) requestBody.get("phone"));

		    return signup(userDTO);
		}


	    private ResponseEntity<?> handleUpdateUser(Map<String, Object> requestBody, HttpServletRequest req) {
	        String userId = (String) requestBody.get("userId");
	        String user_Nnm = (String) requestBody.get("user_Nnm");
	        String phone = (String) requestBody.get("phone");
	        String email = (String) requestBody.get("email");
	        String review = (String) requestBody.get("review");
	        String password = (String) requestBody.get("password"); 
	        MultipartFile profileImage = (MultipartFile) requestBody.get("profileImage"); 
 
	        Map<String, Object> result = updateUser(req, userId, user_Nnm, phone, email, review, password, profileImage);
	        return ResponseEntity.ok(result);
	    }
	   
	    public Map<String, Object> updateUser(HttpServletRequest req, String userId, String user_Nnm, String phone,
                String email, String review, String password, MultipartFile profileImage) {
			Map<String, Object> resultMap = new HashMap<>();
			boolean check = true;
			String profileImageUrl = uploadProfileImage(profileImage);
			
			resultMap.put("state", check);
			if (check) {
			UserDTO userDTO = new UserDTO();
			userDTO.setUserId(userId);
			userDTO.setUser_Nnm(user_Nnm);
			userDTO.setPhone(phone);
			userDTO.setEmail(email);
			userDTO.setReview(review);
			userDTO.setPassword(password);
			userDTO.setProfileImageUrl(profileImageUrl);
			updateUserInDB(userDTO);
			resultMap = getUserData(req);
			}
			return resultMap;
			}
	    
	
    public ResponseEntity<?> viewFile(String url) {
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

    public ResponseEntity<?> login(UserDTO userDTO) {
        UserResultDTO userResultDTO = findByUser(userDTO);
        if (userResultDTO.isStatus()) {
            String jwtToken = setToken(Collections.singletonMap("name", userDTO.getUserId()));
            Map<String, Object> response = new HashMap<>();
            response.put("status", true);
            response.put("user", userResultDTO.getUserDTO());
            response.put("jwtToken", jwtToken);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 틀렸습니다.");
        }
    }

    public ResponseEntity<?> signup(UserDTO userDTO) {
        UserResultDTO userResultDTO = signupUser(userDTO);
        if (userResultDTO.isStatus()) {
            return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
        } else {
            if ("중복된 아이디가 있습니다.".equals(userResultDTO.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", "중복된 아이디가 있습니다."));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", userResultDTO.getMessage()));
        }
    }
 

    private String getUser(String token) {
		return getToken(token).get("name", String.class);
	}
    private Claims getToken(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
    private Map<String, Object> getUserData(HttpServletRequest request) {
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
		        // 사용자 정보 조회--------------------------------------------------------------------------------------------
		        UserResultDTO userResultDTO = findOne(userDTO);
		        // 조회된 사용자 정보 반환 또는 404 Not Found
		        if (userResultDTO.isStatus()) {
		            response.put("status", true);
		            response.put("user", userResultDTO.getUserDTO());
		        } 
	        }
		  } else { 
			  response.put("msg", "사용자 Token이 만료되었습니다.");
      }
      return response;
    } 
    public String getTokenFromHeader(String token) {
	    String[] strArray = token.split(" ");
	    if("Bearer".toUpperCase().equals(strArray[0].toUpperCase())) return strArray[1];
	    return null;
	}
    // Helper methods

    private String uploadProfileImage(MultipartFile profileImage) {
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String fileExtension = getFileExtension(profileImage.getOriginalFilename());
                String fileName = UUID.randomUUID().toString() + "." + fileExtension;
                File destinationFile = new File(uploadDir, fileName);
                profileImage.transferTo(destinationFile);
                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private String setToken(Map<String, String> paramMap) {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, 10);
        Map<String, String> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", paramMap.get("name"));
        return Jwts.builder()
                .setHeader(header)
                .setClaims(userMap)
                .setIssuer("BackProject")
                .setSubject("UserInfo")
                .setExpiration(date.getTime())
                .setIssuedAt(Calendar.getInstance().getTime())
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getTokenClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private SecretKey getKey() {
        String strKey = "c2hlbGxmb2xkZXIxMjM0NTY3ODlEZXZKV1QxMjM0NTY3ODk=";
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(strKey));
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = getTokenClaims(token);
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");
        } catch (JwtException exception) {
            log.error("Token Tampered");
        } catch (NullPointerException exception) {
            log.error("Token is null");
        }
        return false;
    }

    private UserResultDTO findByUser(UserDTO userDTO) {
        UserDTO users = userMapper.findOne(userDTO.getUserId());
        boolean status = false;
        String message = "유효한 사용자가 없습니다.";

        if (users != null) {
            status = true;
            message = users.getUserId() + "님 환영합니다.";
        }

        return UserResultDTO.builder()
                .status(status)
                .message(message)
                .userDTO(users)
                .build();
    }

    @Transactional
    private UserResultDTO signupUser(UserDTO userDTO) {
        UserResultDTO userResultDTO = new UserResultDTO();
        if (userMapper.checkUserIdDuplicate(userDTO.getUserId()) > 0) {
            userResultDTO.setStatus(false);
            userResultDTO.setMessage("중복된 아이디가 있습니다.");
            return userResultDTO;
        }

        if (userMapper.signup(userDTO) > 0) {
            userResultDTO.setStatus(true);
            userResultDTO.setMessage("회원가입이 완료되었습니다.");
            userResultDTO.setUserDTO(userDTO);
        } else {
            userResultDTO.setStatus(false);
            userResultDTO.setMessage("회원가입에 실패하였습니다.");
        }
        return userResultDTO;
    }

    public void updateUserInDB(UserDTO userDTO) {
        userMapper.updateUser(userDTO);
    }

    public UserResultDTO findOne(UserDTO userDTO) {
        UserResultDTO userResultDTO = new UserResultDTO();
        UserDTO user = userMapper.findOne(userDTO.getUserId());

        if (user != null) {
            userResultDTO.setUserDTO(user);
            userResultDTO.setStatus(true);
        } else {
            userResultDTO.setStatus(false);
        }

        return userResultDTO;
    }
}
