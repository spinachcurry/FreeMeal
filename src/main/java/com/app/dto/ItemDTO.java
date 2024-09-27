package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
	
	private String title;
	private String link;
	private String category;
	private String description;
	private String telephone;
	private String address;
	private String roadAddress;
	private int mapx;
	private int mapy;

	
}
