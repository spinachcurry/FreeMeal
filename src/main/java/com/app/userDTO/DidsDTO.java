package com.app.userDTO;

import java.util.List; 
import org.springframework.data.domain.jaxb.SpringDataJaxb.SortDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DidsDTO {
	
	private List<SortDto> Store;
	private String userId;
	private String address;
	private int status;    
	

}
