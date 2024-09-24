package com.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.app.dto.DataDTO;
import com.app.dto.RawDataDTO;

@Mapper
public interface DataMapper {

	@Select("SELECT date, storeNm, party, price, areaNm FROM root_data")
	public List<RawDataDTO> getRawData(); //로데이타에서 합칠 항목만 쏙쏙 셀렉트문
	
	@Insert("INSERT INTO test_freemeal (`title`,`link`, `category`, `description`, `telephone`, `address`, `roadAddress`, `mapx`, `mapy`, `price`,`party`,`visitDate`) "
			+ "VALUE (#{title},#{link},#{category},#{description},#{telephone},#{address},#{roadAddress},#{mapx},#{mapy},#{price},#{party},#{visitDate})")
	public int setOwnData(DataDTO ownDTO); // 기본적인 매뻐 완성!
	
	
}
