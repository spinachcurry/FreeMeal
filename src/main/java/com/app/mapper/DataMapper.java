package com.app.mapper;

import java.util.List;

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
	@Select("SELECT count FROM call_count WHERE `count date` = ${today}")
	public List<Integer> getCount(String today);

	//데이터베이스 메모장~.~
	@Insert("INSERT INTO call_count (`count date`, `count`) VALUES(#{countdate} , #{count}) "
			+ "ON DUPLICATE KEY UPDATE count = #{count}") 
	public int setCount(String countdate, int count);
}
