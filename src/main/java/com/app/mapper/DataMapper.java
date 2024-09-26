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

	@Select("SELECT date, storeNm, party, price, areaNm FROM root_data")
	public List<RawDataDTO> getRawData(); //로데이타에서 합칠 항목만 쏙쏙 셀렉트문
	
	@Insert("INSERT INTO test_freemeal (`title`,`link`, `category`, `description`, `telephone`, `address`, `roadAddress`, `mapx`, `mapy`, `price`,`party`,`visitDate`) "
			+ "VALUE (#{title},#{link},#{category},#{description},#{telephone},#{address},#{roadAddress},#{mapx},#{mapy},#{price},#{party},#{visitDate})")
	public int setOwnData(DataDTO ownDTO); // 기본적인 매뻐 완성!
	
	//하루에 얼마나 카운팅 되었는가
	@Select("SELECT count FROM call_count WHERE `date` = #{today}")
	public List<Integer> getCount(String today);

	//데이터베이스 메모장~.~ insert와 update를 한번에!
	@Insert("INSERT INTO call_count (`date`, `count`) "
			+ "VALUES (#{countdate} , #{count}) "
			+ "ON DUPLICATE KEY UPDATE count = VALUES(count)") 
	public int setCount(Map<String, Object> map);
	
	//광수가 주는 새로운 raw data를 돌릴 때 기존 test freemeal에서 돌린 것들은 제외하고 새로 돌리는 맵핑
	//	@Update("UPDATE root_data SET no = #{no} WHERE no = #{no + 1`})")
	 

}

