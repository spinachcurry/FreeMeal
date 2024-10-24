package com.app.dto;
  
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
	private String category;
	private String imgURL;
	private String price;
	private String name;
	private String roadAddress;
	private String sotreNm;
	private String imageURL;
	
	 

}
