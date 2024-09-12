package com.app.dto;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DataDTO {
	
	int no;
	String title;
	String link;
	String category;
	String description;
	String telephone;
	String address;
	String roadAddress;
	int mapx;
	int mapy;
	int price;
	int party;
	
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	Date visitDate;
}
