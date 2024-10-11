package com.app.service;

import java.io.Console;
import java.util.HashMap;
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
	
	public Map<String, List<StoreDTO>> storeNearby(Map<String, Object> location) {
		Map<String, List<StoreDTO>> bigMap = new HashMap<>(); 
		
		double range = 0.5;
		Map<String, Double> nearMap = new HashMap<>();
		System.out.println("------" + nearMap);
		log.info("--1" + nearMap);
		
		nearMap.put("maxLng", Double.valueOf(location.get("longitude").toString()) + range);	
		nearMap.put("minLng", Double.valueOf(location.get("longitude").toString()) - range);	
		nearMap.put("maxLat", Double.valueOf(location.get("latitude").toString()) + range);	
		nearMap.put("minLat", Double.valueOf(location.get("latitude").toString()) - range);	
		
		log.info("Calculated nearMap values: {}", nearMap);
		bigMap.put("nearbyStore", storeMapper.storeNearby(nearMap));
		bigMap.put("highPrice", storeMapper.highPrice());
		bigMap.put("footStores", storeMapper.footStores());
		return bigMap;
	}

	
}
	
	
