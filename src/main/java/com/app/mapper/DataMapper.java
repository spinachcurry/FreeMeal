package com.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.app.dto.DataDTO;
import com.app.dto.RawDataDTO;

@Mapper
public interface DataMapper {
	
	// 기본적인 매뻐 완성!
	@Insert("INSERT INTO freemeal (`title`,`link`, `category`, `description`, `telephone`, `address`, `roadAddress`, `lng`, `lat`, `price`,`party`,`visitDate`,`areaNm` ) "
			+ "VALUE (#{title},#{link},#{category},#{description},#{telephone},#{address},#{roadAddress},#{lng},#{lat},#{price},#{party},#{visitDate},#{areaNm})")
	public int setOwnData(DataDTO ownDTO); 
	
	//로데이타에서 합칠 항목만 쏙쏙 셀렉트문
	@Select("SELECT `no`, `date`, `storeNm`, `party`, `price`, `areaNm` FROM `root_data` WHERE `check` = 0")
	public List<RawDataDTO> getRawData(); 
	
	//하루에 얼마나 카운팅 되었는가
	@Select("SELECT count FROM call_count WHERE `date` = #{today}")
	public List<Integer> getCount(String today);

	//데이터베이스 메모장~.~ insert와 update를 한번에!
	@Insert("INSERT INTO call_count (`date`, `count`) "
			+ "VALUES (#{countdate} , #{count}) "
			+ "ON DUPLICATE KEY UPDATE `count` = #{count}") 
	public int setCount(Map<String, Object> map);
	
	//광수가 주는 새로운 raw data를 돌릴 때 기존 test freemeal에서 돌린 것들은 제외하고 새로 돌리는 맵핑
	@Update("UPDATE root_data SET `check` = 1 WHERE `no` = #{no}")
	public int check(int no);
	
	//mapx, mapy 소수점 단위 맞춤 뭐리
	@Update("UPDATE freemeal SET lng = (lng / 10000000), lat = (lat / 10000000)")
	public int mapLocation();
	
	
}

