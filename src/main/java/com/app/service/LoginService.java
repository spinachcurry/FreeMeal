package com.app.service;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.app.mapper.UserMapper;
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
	
	public UserResultDTO signup(UserDTO userDTO) {
	    UserResultDTO userResultDTO = new UserResultDTO();

	    try {
	        // 아이디 중복 체크
	        int count = userMapper.checkUserIdDuplicate(userDTO.getUserId());
	        if (count > 0) {
	            // 중복된 아이디가 있을 경우 실패 처리
	            log.info("중복된 아이디: {}", userDTO.getUserId());
	            userResultDTO.setStatus(false);
	            userResultDTO.setMessage("중복된 아이디가 있습니다.");
	            return userResultDTO;
	        }

	        // 중복이 없을 경우 회원가입 처리
	        int result = userMapper.signup(userDTO);
	        log.info("---Mapper에서 받은 반환값: {}---", result);

	        if (result > 0) {
	            // 성공 시 결과 설정
	            userResultDTO.setStatus(true);
	            userResultDTO.setMessage("회원가입이 완료되었습니다.");
	            userResultDTO.setUserDTO(userDTO); // 등록된 사용자 정보 설정
	        } else {
	            // 실패 시 결과 설정
	            userResultDTO.setStatus(false);
	            userResultDTO.setMessage("회원가입에 실패하였습니다.");
	        }
	    } catch (Exception e) {
	        log.error("---회원가입 실패 이유: {}", e.getMessage(), e);
	        userResultDTO.setStatus(false);
	        userResultDTO.setMessage("회원가입에 실패하였습니다: " + e.getMessage());
	    }

	    return userResultDTO;
	}
	
	
	public UserResultDTO findOne(UserDTO userDTO) {
	    UserResultDTO userResultDTO = new UserResultDTO();

	    // 매퍼 호출하여 사용자 정보 가져오기
	    List<UserDTO> userInfo = userMapper.findOne(userDTO.getUserId());

	    if (userInfo != null && !userInfo.isEmpty()) {
	        UserDTO user = userInfo.get(0);  // 첫 번째 사용자의 정보를 가져옴

	        // UserResultDTO에 정보 채우기
	        userResultDTO.setUserDTO(user);  // UserDTO 객체를 UserResultDTO에 추가
	        userResultDTO.setStatus(true);  // 찾은 결과가 있으므로 상태를 true로 설정
	    } else {
	        userResultDTO.setStatus(false);  // 사용자를 찾지 못했을 경우 상태를 false로 설정
	    }

	    return userResultDTO;
	}
	
	
}
