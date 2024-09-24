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
public class RawDataDTO {
	
//	date storeNm party price areaNm

	String storeNm;
	int price;
	int party;
	String areaNm;
	
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	Date date;
}
