package com.app.userDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResultDTO {

	private boolean status;
	private String message;
	private UserDTO userDTO;
	
}
