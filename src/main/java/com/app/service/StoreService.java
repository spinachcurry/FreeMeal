package com.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.StoreDTO;
import com.app.mapper.StoreMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StoreService {

	@Autowired
	private StoreMapper storeMapper;
	
	//가게 상세 페이지
	public StoreDTO storeDetail(String title) {
			log.info("나오세요 : {}");
			List<StoreDTO> list = storeMapper.storeDetail(title);
			if(list.size() < 1) {
				return StoreDTO.builder()
						.title("그런 가게는 없습니다.")
						.build();
			}else {
				return list.get(0);
			}
	}
	
	//메인페이지 >> 내 근처 가게
	public StoreDTO storeNearby(Map<String, Double> location) {
		log.info("나오는지 확인 : {}");
		List<StoreDTO> list = storeMapper.storeNearby(location);
			return StoreDTO.builder()
							.lat(1)
							.lng(1)
							.build();
	}

}
	
	
