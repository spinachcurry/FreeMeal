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
			+ "FROM freeMeal "
			+ "GROUP BY `title`, `areaNm`")
	public List<StoreDTO> storeList();
	
	//가게 상세 페이지
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freeMeal "
			+ "WHERE title = #{title} and areaNm = #{areaNm} " 
			+ "GROUP BY `title`, `areaNm`" )
	public List<StoreDTO> storeDetail(Object object);
	
	//가게 상세 페이지 >> 가격별(내림차순) 정렬 쿼리
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freeMeal GROUP BY `title`, `areaNm` "
			+ "ORDER BY `totalPrice` DESC limit 20")
	public List<StoreDTO> highPrice();
	
	//가게 상세 페이지 >> 방문별 쿼리
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freeMeal GROUP BY `title`, `areaNm` "
			+ "ORDER BY `totalParty` DESC limit 20")
	public List<StoreDTO> footStores();
	
	//내 근처 가게 목록
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freeMeal "
			+ "WHERE lng < #{maxLng} AND lng > #{minLng} AND lat < #{maxLat} AND lat > #{minLat} " 
			+ "GROUP BY `title`, `areaNm` limit 20")
	public List<StoreDTO> storeNearby(Map<String, Double> location);

	//가게명이나 카테고리 검색(ex: 강동구 카페) >> 방문자 순
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal "
			+ "WHERE ((`title` LIKE #{keyword} OR `category` LIKE #{keyword}) " 
			+ "OR (`title`, `areaNm`) IN (SELECT `storeNm`, `areaNm` FROM store_menu WHERE `name` LIKE #{keyword} GROUP BY `storeNm`, `areaNm`)) "
			+ "AND `areaNm` = #{areaNm} "
			+ "GROUP BY `title`, `areaNm` "
			+ "ORDER BY `totalParty` DESC ")
	public List<StoreDTO> searchByStoreParty(Map<String, Object> keykeyword);
	
	//가게명이나 카테고리 검색(ex: 강동구 카페) >> 돈 쓴 순
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal "
			+ "WHERE ((`title` LIKE #{keyword} OR `category` LIKE #{keyword}) "
			+ "OR (`title`, `areaNm`) IN (SELECT `storeNm`, `areaNm` FROM store_menu WHERE `name` LIKE #{keyword} GROUP BY `storeNm`, `areaNm`)) "
			+ "AND `areaNm` = #{areaNm} "
			+ "GROUP BY `title`, `areaNm` "
			+ "ORDER BY `totalPrice` DESC ")
	public List<StoreDTO> searchByStoreCash(Map<String, Object> keykeyword);
	
	//전체창에서 검색>> 지역 선택 X >> 방문자 순
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal "
			+ "WHERE `title` LIKE #{keyword} OR `category` LIKE #{keyword} "
			+ "OR (`title`, `areaNm`) IN (SELECT `storeNm`, `areaNm` FROM store_menu WHERE `name` LIKE #{keyword} GROUP BY `storeNm`, `areaNm`) "
			+ "GROUP BY `title`, `areaNm` "
			+ "ORDER BY `totalParty` DESC ")
	public List<StoreDTO> searchAllStoreParty(Map<String, Object> keyword);
	
	//전체창에서 검색>> 지역 선택 X  >> 돈 쓴 순
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM freemeal "
			+ "WHERE `title` LIKE #{keyword} OR `category` LIKE #{keyword} "
			+ "OR (`title`, `areaNm`) IN (SELECT `storeNm`, `areaNm` FROM store_menu WHERE `name` LIKE #{keyword} GROUP BY `storeNm`, `areaNm`) "
			+ "GROUP BY `title`, `areaNm` "
			+ "ORDER BY `totalPrice` DESC ")
	public List<StoreDTO> searchAllStoreCash(Map<String, Object> keyword);
	
	//평균, 표준편차
	@Select("SELECT AVG(`totalPrice`) AS `MeanOfPrice`, STDDEV(`totalPrice`) AS `StdOfPrice`,"
			+ "AVG(`totalParty`) AS `MeanOfParty`, STDDEV(`totalParty`) AS `StdOfParty` "
			+ "FROM ( "
			+ "SELECT * "
			+ "FROM (SELECT `title`, `areaNm`, SUM(`price`) AS `totalPrice`, SUM(`party`) AS `totalParty` "
			+ "FROM freeMeal "
			+ "WHERE `areaNm` = #{areaNm} "
			+ "GROUP BY `title`, `areaNm`) AS `originalTable` "
			+ "WHERE `totalPrice` > 999) AS `tableOfFreeMeal`")
	public Map<String, Double> getStatistics(String areaNm);
}	

