package com.app.userDTO;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private List<RoleDTO> userRoles; 
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
