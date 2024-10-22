package com.app.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data  
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 DTO")
public class UserDTO {
	
	private RoleDTO userRoles; 
	private int userNo;
	private String userId;
    private String password;
    private String name;
    private String user_Nnm;
    private String phone;
    private String email;
    private LocalDateTime createDate;  // 읽기 전용으로 사용할 수 있음
    private LocalDateTime modifiedDate; // 읽기 전용으로 사용할 수 있음
    private String status;
    private String review;
    private String profileImageUrl;

}
