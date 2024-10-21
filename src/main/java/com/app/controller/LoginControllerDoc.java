package com.app.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.dto.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="LoginControllerDoc", description = "로그인 URL 확인.")
public interface LoginControllerDoc {

	@Operation(summary = "파일 URL 요청", description = "파일 업로드 중 사용자 이미지 가져 오기 위한 주소")
	public ResponseEntity<?> view(@RequestParam("url") String url);
	
	@Operation(summary = "사용자 로그인", description = "회원 로그인 요청")
//	@Parameters(value = {
//			@Parameter(name="userId", description = "사용자 아이디")
//	})
	public ResponseEntity<?> login(@RequestBody UserDTO userDTO);
	
}
