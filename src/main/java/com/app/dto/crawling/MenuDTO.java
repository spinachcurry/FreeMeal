package com.app.dto.crawling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
	private String menu;
	private String category;
	private String price;
	private String image;
	private String description;
}
