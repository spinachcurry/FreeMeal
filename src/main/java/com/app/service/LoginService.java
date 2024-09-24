package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.mapper.UserMapper;
import com.app.userDTO.RoleDTO;
import com.app.userDTO.UserDTO;
import com.app.userDTO.UserResultDTO;
 

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
}
