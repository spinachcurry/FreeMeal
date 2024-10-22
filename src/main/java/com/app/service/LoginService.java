package com.app.service;

import com.app.dto.RoleDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;
 
 //  파일을 클라이언트에 반환하는 메서드 */
    public ResponseEntity<?> getFileAsResponse(String url) {
        try {
            String path = uploadDir.concat("/").concat(url);
            File file = new File(path);

            if (file.exists()) {
                return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType(Files.probeContentType(file.toPath()))) // 동적 MIME 타입 설정
                    .body(new InputStreamResource(new FileInputStream(file)));
            } else {
                return ResponseEntity.notFound().build(); // 파일이 없을 경우 404 반환
            }
        } catch (Exception e) {
            log.error("파일 불러오기 실패: ", e);
            return ResponseEntity.status(500).body("파일을 불러오는 중 오류가 발생했습니다.");
        }
    } 
     //  사용자 로그인 처리 
    public Map<String, Object> login(UserDTO userDTO) {
        UserResultDTO userResultDTO = findByUser(userDTO);
        Map<String, Object> response = new HashMap<>();

        if (userResultDTO.isStatus()) {
            String jwtToken = generateToken(Collections.singletonMap("name", userDTO.getUserId()));
            response.put("status", true);
            response.put("user", userResultDTO.getUserDTO());
            response.put("jwtToken", jwtToken);
        } else {
            response.put("status", false);
            response.put("message", "아이디 또는 비밀번호가 틀렸습니다.");
        }

        return response;
    }  
    public UserResultDTO findByUser(UserDTO userDTO) {
        UserDTO users = userMapper.findOne(userDTO.getUserId());

        boolean status = false;
        String message = "유효한 사용자가 없습니다.";

        if (users != null) {
            status = true;
            message = users.getUserId() + "님 환영합니다.";

            // 사용자 권한 목록 설정
            RoleDTO roles = userMapper.findByRoles(users.getUserNo());
            users.setUserRoles(roles);
        }

        return UserResultDTO.builder()
            .status(status)
            .message(message)
            .userDTO(users)
            .build();
    }

    @Transactional
    public UserResultDTO signup(UserDTO userDTO) {
        UserResultDTO userResultDTO = new UserResultDTO();
        if (userMapper.checkUserIdDuplicate(userDTO.getUserId()) > 0) {
            userResultDTO.setStatus(false);
            userResultDTO.setMessage("중복된 아이디가 있습니다.");
            return userResultDTO;
        }

        // 기본 설정값 추가
        userDTO.setProfileImageUrl(userDTO.getProfileImageUrl() != null ? userDTO.getProfileImageUrl() : "/uploads/default.png");
        userDTO.setReview(userDTO.getReview() != null ? userDTO.getReview() : "This is a sample review.");
        userDTO.setStatus(userDTO.getStatus() != null ? userDTO.getStatus() : "1");

        int result = userMapper.signup(userDTO);
        if (result > 0) {
            userResultDTO.setStatus(true);
            userResultDTO.setMessage("회원가입이 완료되었습니다.");
            userResultDTO.setUserDTO(userDTO);
        } else {
            userResultDTO.setStatus(false);
            userResultDTO.setMessage("회원가입에 실패하였습니다.");
        }

        return userResultDTO;
    }
     // 사용자 정보 업데이트 처리
    public Map<String, Object> updateUserProfile(HttpServletRequest req, String userId, String user_Nnm, String phone, String email,
                                                 String review, String password, MultipartFile profileImage, String status) {
        Map<String, Object> resultMap = new HashMap<>();
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setUser_Nnm(user_Nnm);
        userDTO.setPhone(phone);
        userDTO.setEmail(email);
        userDTO.setReview(review);
        userDTO.setPassword(password);
        userDTO.setStatus(status);

        try {
            if (profileImage != null && !profileImage.isEmpty()) {
                userDTO.setProfileImageUrl(saveProfileImage(profileImage));
            }
            userMapper.updateUser(userDTO);
            resultMap.put("status", true);
            resultMap.put("user", userDTO);
        } catch (Exception e) {
            log.error("업데이트 중 오류 발생: ", e);
            resultMap.put("status", false);
            resultMap.put("message", "업데이트에 실패했습니다: " + e.getMessage());
        }
        return resultMap;
    }
    // 프로필 이미지 저장 메서드
    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + getFileExtension(profileImage.getOriginalFilename());
        Path destinationDir = Paths.get(uploadDir);
        if (!Files.exists(destinationDir)) {
            Files.createDirectories(destinationDir); // 디렉토리가 없으면 생성
        }
        File destinationFile = new File(destinationDir.toString(), fileName);
        profileImage.transferTo(destinationFile);
        return fileName;
    }

    private String getFileExtension(String originalFilename) {
        return originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                : "";
    }
    // 토큰 생성
    public String generateToken(Map<String, String> paramMap) {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, 10);

        return Jwts.builder()
                .setClaims(paramMap)
                .setIssuer("BackProject")
                .setExpiration(date.getTime())
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("name", String.class);
    }

    private SecretKey getSecretKey() {
        String strKey = "c2hlbGxmb2xkZXIxMjM0NTY3ODlEZXZKV1QxMjM0NTY3ODk=";
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(strKey));
    }
    // 사용자 정보 조회
    public UserResultDTO findOne(UserDTO userDTO) {
        UserDTO user = userMapper.findOne(userDTO.getUserId());
        UserResultDTO userResultDTO = new UserResultDTO();

        if (user != null) {
            RoleDTO roles = userMapper.findByRoles(user.getUserNo());
            user.setUserRoles(roles);
            userResultDTO.setUserDTO(user);
            userResultDTO.setStatus(true);
        } else {
            userResultDTO.setStatus(false);
        }

        return userResultDTO;
    }
}
