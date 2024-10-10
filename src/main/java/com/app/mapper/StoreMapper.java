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
			+ "FROM test_freemeal "
			+ "GROUP BY `title`, `areaNm`")
	public List<StoreDTO> storeList();
	
	//가게 상세 페이지
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM test_freemeal "
			+ "WHERE title = #{title} " 
			+ "GROUP BY `title`, `areaNm`" )
	public List<StoreDTO> storeDetail(String title);
	
	//내 근처 가게 목록
	@Select("SELECT `title`,`link`, `telephone`, `areaNm`, `lng`, `lat`, `address`, `roadAddress`, `category`, `description`, "
			+ "SUM(`price`) AS totalPrice, SUM(`party`) AS totalParty "
			+ "FROM test_freemeal "
			+ "WHERE lng < #{maxLng} AND lng > #{minLng} AND lat < #{maxLat} AND lat > #{minLat} " 
			+ "GROUP BY `title`, `areaNm`")
	public List<StoreDTO> storeNearby(Map<String, Double> location);
}

