package com.app.dto.crawling;

import java.util.List;

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
@NoArgsConstructor
@AllArgsConstructor
public class KageDTO {
	private String storeNm;
	private List<MenuDTO> menu;
	private List<String> imgURLs;
}
