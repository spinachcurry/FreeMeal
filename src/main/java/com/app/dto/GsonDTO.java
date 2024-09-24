package com.app.dto;

import java.util.List;

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
public class GsonDTO {
	
	private String lastBuildDate;
	private String total;
	private String start;
	private String display;
	private List<ItemDTO> items;
	
	
}
