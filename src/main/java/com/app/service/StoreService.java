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

		nearMap.put("maxLng", (double)location.get("longitude") + range);	
		nearMap.put("minLng", (double)location.get("longitude") - range);	
		nearMap.put("maxLat", (double)location.get("latitude") + range);	
		nearMap.put("minLat", (double)location.get("latitude") - range);	
//		log.info("여긴 어때?: {}", map);
//		log.info("가게: {}", storeMapper.storeNearby(map));
		
		bigMap.put("nearbyStore", getReadyForFront(storeMapper.storeNearby(nearMap)));
		bigMap.put("highPrice", getReadyForFront(storeMapper.highPrice()));		
		bigMap.put("footStores", getReadyForFront(storeMapper.footStores()));
		
		return bigMap;
	}

	//메인 페이지 검색>> 가게명 or 지역 검색(ex 강동구 카페)
	public List<StoreDTO> searchStore(Map<String, Object> keykeyword) {
		keykeyword.replace("keyword", "%" + keykeyword.get("keyword") + "%");
		if("전체".equals(keykeyword.get("areaNm"))) {
			log.info("keykeyword:{}", keykeyword.toString());
			if("party".equals(keykeyword.get("criteria")))
				return getReadyForFront(storeMapper.searchAllStoreParty(keykeyword));
			else
				return getReadyForFront(storeMapper.searchAllStoreCash(keykeyword));
		}else {
			log.info("keyword:{}", keykeyword.toString());
			if("party".equals(keykeyword.get("criteria")))
				return getReadyForFront(storeMapper.searchByStoreParty(keykeyword));
			else
				return getReadyForFront(storeMapper.searchByStoreCash(keykeyword));
		}
	}
	
	private List<StoreDTO> getReadyForFront(List<StoreDTO> stores){
		Map<String, Double> statistics = storeMapper.getStatistics("강남구");
		double meanOfPrice = Double.parseDouble(String.valueOf(statistics.get("MeanOfPrice")));
		double stdOfPrice = Double.parseDouble(String.valueOf(statistics.get("StdOfPrice")));
		double meanOfParty = Double.parseDouble(String.valueOf(statistics.get("MeanOfParty")));
		double stdOfParty = Double.parseDouble(String.valueOf(statistics.get("StdOfParty")));
		for(StoreDTO store : stores) {
			store.setMenuItems(getstoreMapper.getStoreMenu(store));
			store.setImgURLs(getstoreMapper.getStoreImg(store));
			store.setBills(2.5 +
					((double)store.getTotalPrice() - meanOfPrice)/stdOfPrice
						);
			store.setFeet(2.5 +
					((double)store.getTotalParty() - meanOfParty)/stdOfParty
						);
			log.info("가게 정보: {}", store);
		}
		return stores;
	}	
	
}

