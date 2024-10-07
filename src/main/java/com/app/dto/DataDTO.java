package com.app.dto;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
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
	String areaNm;
	double lng;
	double lat;
	int price;
	int party;
	
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	Date visitDate;
}
