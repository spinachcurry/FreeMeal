package com.app.dto;
   
import java.util.List;

import com.app.dto.crawling.MenuDTO;

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
	private int totalPrice;
	private String title;
	private String areaNm;	
	private String category; 	
	private List<MenuDTO> menuItems;
	private List<String> imgURLs;
}
