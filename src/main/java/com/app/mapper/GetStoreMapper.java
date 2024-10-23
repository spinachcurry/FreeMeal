package com.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.app.dto.StoreDTO;
import com.app.dto.crawling.MenuDTO;

@Mapper
public interface GetStoreMapper {
	
	@Select("select 1 as no")
	public int test();
	
	//특정 가게 메뉴판(이미지) 가져옴
	@Select("SELECT * FROM store_menu WHERE `storeNm`=#{title} AND `areaNm` = #{areaNm} limit 50")
	public List<MenuDTO> getStoreMenu(StoreDTO store);
	
	//특정 가게 이미지(업체 제공 이미지 >> 메뉴 사진 or 매장 사진 or 사장 얼굴 등)
	@Select("SELECT `imageURL` FROM store_image WHERE `storeNm`=#{title} AND `areaNm` = #{areaNm} limit 50")
	public List<String> getStoreImg(StoreDTO storeImg);


}

