package com.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.StoreDTO;
import com.app.mapper.GetStoreMapper;
import com.app.mapper.StoreMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StoreService {

	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private GetStoreMapper getstoreMapper;
	
	//가게 상세 페이지
	public StoreDTO storeDetail(String title) {
			log.info("나오세요 : {}");
			List<StoreDTO> list = storeMapper.storeDetail(title);
			if(list.size() < 1) {
				return StoreDTO.builder()
						.title("")
						.build();
			}else {
				return list.get(0);
			}
	}
	
	//메인페이지 >> 내 근처 가게
	public Map<String, List<StoreDTO>> storeNearby(Map<String, Object> location){
		
		Map<String, List<StoreDTO>> bigMap = new HashMap<>(); 
		
		double range = 0.5;
		Map<String, Double> nearMap = new HashMap<>();
		nearMap.put("maxLng", (double)location.get("longitude") + range);	
		nearMap.put("minLng", (double)location.get("longitude") - range);	
		nearMap.put("maxLat", (double)location.get("latitude") + range);	
		nearMap.put("minLat", (double)location.get("latitude") - range);	
//		log.info("여긴 어때?: {}", map);
//		log.info("가게: {}", storeMapper.storeNearby(map));
		
		bigMap.put("nearbyStore", setMenuAndImages(storeMapper.storeNearby(nearMap)));
		bigMap.put("highPrice", setMenuAndImages(storeMapper.highPrice()));		
		bigMap.put("footStores", setMenuAndImages(storeMapper.footStores()));
			
		return bigMap;
	}
	
	//메인 페이지 검색>> 가게명 or 지역 검색(ex 강동구 카페)
	public List<StoreDTO> searchStore(Map<String, Object> keykeyword) {
		keykeyword.replace("keyword", "%" + keykeyword.get("keyword") + "%");
		if("전체".equals(keykeyword.get("areaNm"))) {
			log.info("keykeyword:{}", keykeyword.toString());
			if("party".equals(keykeyword.get("criteria")))
				return setMenuAndImages(storeMapper.searchAllStoreParty(keykeyword));
			else
				return setMenuAndImages(storeMapper.searchAllStoreCash(keykeyword));
		}else {
			log.info("keyword:{}", keykeyword.toString());
			if("party".equals(keykeyword.get("criteria")))
				return setMenuAndImages(storeMapper.searchByStoreParty(keykeyword));
			else
				return setMenuAndImages(storeMapper.searchByStoreCash(keykeyword));
		}
	}
	
	private List<StoreDTO> setMenuAndImages(List<StoreDTO> stores){
		for(StoreDTO store : stores) {
			store.setMenuItems(getstoreMapper.getStoreMenu(store));
			store.setImgURLs(getstoreMapper.getStoreImg(store));
		}
		return stores;
	}
		
}