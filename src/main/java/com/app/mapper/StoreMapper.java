package com.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.app.dto.StoreDTO;

@Mapper
public interface StoreMapper {
	
	@Select("select 1 as no")
	public int test();
	
	//전체 가게 목록
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal "
			+ "GROUP BY `title`, `areaNm`")
	public List<StoreDTO> storeList();
	
	//가게 상세 페이지
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal "
			+ "WHERE title = #{title} " 
			+ "GROUP BY `title`, `areaNm`" )
	public List<StoreDTO> storeDetail(String title);
	
	//가게 상세 페이지 >> 가격별(내림차순) 정렬 쿼리
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal GROUP BY `title`, `areaNm` "
			+ "ORDER BY `totalPrice` DESC limit 10")
	public List<StoreDTO> highPrice();
	
	//가게 상세 페이지 >> 방문별 쿼리
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal GROUP BY `title`, `areaNm` "
			+ "ORDER BY `totalParty` DESC limit 10")
	public List<StoreDTO> footStores();
	
	//내 근처 가게 목록
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal "
			+ "WHERE lng < #{maxLng} AND lng > #{minLng} AND lat < #{maxLat} AND lat > #{minLat} " 
			+ "GROUP BY `title`, `areaNm` limit 10")
	public List<StoreDTO> storeNearby(Map<String, Double> location);

	//가게명이나 카테고리 검색(ex: 강동구 카페)
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal "
			+ "WHERE (`title` LIKE #{keyword} OR `category` LIKE #{keyword}) AND `areaNm` = #{areaNm} " 
			+ "GROUP BY `title`, `areaNm` ")
	public List<StoreDTO> searchStore(Map<String, Object> keykeyword);
	
	//전체 검색
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal "
			+ "WHERE `title` LIKE #{keyword} OR `category` LIKE #{keyword} " 
			+ "GROUP BY `title`, `areaNm` ")
	public List<StoreDTO> searchStore2(Object keyword);
	
	//가게 정보가 담긴 링크 가져오기^^;
//	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
//			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
//			+ "FROM freemeal "
//			+ "WHERE `title` LIKE #{storeinfo} AND `areaNm` LIKE #{storeinfo} " 
//			+ "GROUP BY `title`, `areaNm` ")
//	public List<StoreDTO> storeLink(Object storeinfo);
	
}