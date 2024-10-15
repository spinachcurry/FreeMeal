package com.app.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.app.mapper.UserMapper;
import com.app.userDTO.DidsDTO;
import com.app.userDTO.ReviewDTO;
import com.app.userDTO.RoleDTO;
import com.app.userDTO.UserDTO;
import com.app.userDTO.UserResultDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
 
@Service
@Slf4j
public class LoginService {
	
	@Autowired
	private UserMapper userMapper;
	private ObjectMapper objectMapper;
	
	    public LoginService(UserMapper userMapper, ObjectMapper objectMapper) {
	        this.userMapper = userMapper;
	        this.objectMapper = objectMapper;
	        this.objectMapper.registerModule(new JavaTimeModule()); // Java 8 Time Module 등록
	    }
	
	    public UserResultDTO findByUser(UserDTO userDTO) {
	    // 매퍼를 호출하여 사용자 정보를 가져옴
	    List<UserDTO> users = userMapper.findOne(userDTO.getUserId()); // findOne 매퍼 호출

	    boolean status = false;
	    String message = "유효한 사용자가 없습니다.";
	    UserDTO foundUser = null;

	    if (users != null && !users.isEmpty()) {
	        foundUser = users.get(0); // 첫 번째 사용자 정보 가져오기
	        status = true;
	        message = foundUser.getUserId() + "님 환영합니다.";

	        // 사용자 권한 목록 가져오기
	        List<RoleDTO> roles = userMapper.findByRoles(foundUser.getStatus());
	        foundUser.setUserRoles(roles); // 권한 설정
	    }

	    return UserResultDTO.builder()
	            .status(status)
	            .message(message)
	            .userDTO(foundUser)
	            .build();
	} 
	
	    @Transactional
	    public UserResultDTO signup(UserDTO userDTO) {
	        UserResultDTO userResultDTO = new UserResultDTO();

	        // 기본값 설정
	        if (userDTO.getStatus() == null) {
	            userDTO.setStatus("1");
	        }
	        if (userDTO.getReview() == null) {
	            userDTO.setReview("This is a sample review.");
	        }
	        if (userDTO.getProfileImageUrl() == null) {
	            userDTO.setProfileImageUrl("/uploads/default.png");
	        }

	        try {
	            // 아이디 중복 체크
	            int count = userMapper.checkUserIdDuplicate(userDTO.getUserId());
	            if (count > 0) {
	                // 중복된 아이디일 경우, 실패로 처리
	                userResultDTO.setStatus(false);
	                userResultDTO.setMessage("중복된 아이디가 있습니다.");
	                return userResultDTO;
	            }

	            // 회원가입 처리
	            int result = userMapper.signup(userDTO);
	            if (result > 0) {
	                // 성공적으로 가입된 경우
	                userResultDTO.setStatus(true);
	                userResultDTO.setMessage("회원가입이 완료되었습니다.");
	                userResultDTO.setUserDTO(userDTO); // 등록된 사용자 정보 설정
	            } else {
	                // 실패한 경우
	                userResultDTO.setStatus(false);
	                userResultDTO.setMessage("회원가입에 실패하였습니다.");
	            }
	        } catch (Exception e) {
	            // 예외 발생 시 로그로 기록하고 실패 메시지 설정
	            log.error("회원가입 중 오류 발생 - 파라미터 값: {}", userDTO, e);
	            userResultDTO.setStatus(false);
	            userResultDTO.setMessage("회원가입에 실패하였습니다. 오류 메시지: " + e.getMessage());
	        }

	        return userResultDTO;
	    } 
	    
	    public UserResultDTO findOne(UserDTO userDTO) {
	        UserResultDTO userResultDTO = new UserResultDTO();

	        // 매퍼 호출하여 사용자 정보 가져오기
	        List<UserDTO> userInfo = userMapper.findOne(userDTO.getUserId());

	        if (userInfo != null && !userInfo.isEmpty()) {
	            UserDTO user = userInfo.get(0); // 첫 번째 사용자의 정보를 가져옴

	            // 사용자 권한 목록 가져오기
	            List<RoleDTO> roles = userMapper.findByRoles(user.getStatus()); // 권한 정보 조회
	            user.setUserRoles(roles); // UserDTO 객체에 권한 설정

	            // UserResultDTO에 정보 채우기
	            userResultDTO.setUserDTO(user);
	            userResultDTO.setStatus(true); // 찾은 결과가 있으므로 상태를 true로 설정
	        } else {
	            userResultDTO.setStatus(false); // 사용자를 찾지 못했을 경우 상태를 false로 설정
	        }

	        return userResultDTO;
	    }
 //유저정보 수정
  public void updateUser(UserDTO userDTO) {
        userMapper.updateUser(userDTO);
    }   

}
