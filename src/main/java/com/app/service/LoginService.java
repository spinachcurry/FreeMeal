package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.mapper.UserMapper;
import com.app.userDTO.RoleDTO;
import com.app.userDTO.UserDTO;
import com.app.userDTO.UserResultDTO;

import lombok.extern.slf4j.Slf4j;
 
@Service
@Slf4j
public class LoginService {
	@Autowired
	private UserMapper userMapper;
	
	public UserResultDTO findByUser(UserDTO userDTO) {
		userDTO = userMapper.findByUser(userDTO);
		boolean status = false;
		String message = "유효한 사용자가 없습니다.";
		if(userDTO != null) {
			status = true;
			message = userDTO.getUserId() + "님 환영합니다.";
			// 사용자 권한 목록 가져오기!
			List<RoleDTO> roles = userMapper.findByRoles(userDTO.getStatus());
			userDTO.setUserRoles(roles);
		}
		return UserResultDTO.builder()
				.status(status)
				.message(message)
				.userDTO(userDTO)
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
	
	
}
