package com.app.userDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DidsDTO {
	
	private String userId;
	private String address;
	private int status;    
	

}
