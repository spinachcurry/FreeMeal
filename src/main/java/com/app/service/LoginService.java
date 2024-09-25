package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	    UserResultDTO result = new UserResultDTO();
	    
	    // 사용자 추가 로직
	    int insertedRows = userMapper.signup(userDTO);
	    
	    if (insertedRows > 0) {
	        result.setStatus(true);
	        result.setMessage("회원가입이 성공했습니다."); // 성공 메시지
	    } else {
	        result.setStatus(false);
	        result.setMessage("회원가입에 실패했습니다."); // 실패 메시지
	    }
	    return result;
	}
//	public UserResultDTO signup(UserDTO userDTO) {
//	    UserResultDTO userResultDTO = new UserResultDTO();
//	    log.info("---서비스단을 타고 있습니다.---");
//	    try {
//	        // 매퍼를 사용하여 사용자 등록
//	    	log.info("---실패 1번을 탄다..---");
//	        userMapper.signup(userDTO); 
//	        // 성공 시 결과 설정
//	        userResultDTO.setStatus(true);
//	        userResultDTO.setMessage("회원가입이 완료되었습니다."); // 성공 메시지
//	        userResultDTO.setUserDTO(userDTO); // 등록된 사용자 정보 설정
//	    } catch (Exception e) {
//	        // 실패 시 결과 설정
//	    	log.info("---실패 2번을 탄다.---");
//	        userResultDTO.setStatus(false);
//	        userResultDTO.setMessage("회원가입에 실패하였습니다: " + e.getMessage()); // 실패 메시지
//	    }
//
//	    return userResultDTO; // 결과 반환
//	}
	
	
}
