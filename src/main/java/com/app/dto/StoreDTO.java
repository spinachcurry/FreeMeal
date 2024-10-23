package com.app.dto;

import java.util.List;

import com.app.dto.crawling.MenuDTO;

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
public class StoreDTO {
	
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
	int totalPrice;
	int totalParty;
	private List<MenuDTO> menuItems;
	private List<String> imgURLs;
}