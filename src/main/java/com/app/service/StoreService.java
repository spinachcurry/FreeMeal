package com.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.StatisticsDTO;
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
	public StoreDTO storeDetail(Map<String, Object> map) {
			log.info("나오세요 : {}", map.get("params"));
			List<StoreDTO> list = getReadyForFront(storeMapper.storeDetail(map.get("params")));
			if(list.size() < 1) {
				return StoreDTO.builder()
						.title("그런 가게는 없습니다.")
						.build();
			}else {
				log.info("가져 왔나? : {}", list);
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
	public Map<String, Object> searchStore(Map<String, Object> keykeyword) {
		//테스트용
		keykeyword.put("offset", 0);
		keykeyword.put("size", 12);
		
		Map<String, Object> storeMap = new HashMap<>();
		storeMap.put("offset", Integer.parseInt(String.valueOf(keykeyword.get("offset"))) + Integer.parseInt(String.valueOf(keykeyword.get("size"))));
		storeMap.put("storeData", getReadyForFront(storeMapper.searchStore(keykeyword)));
		return storeMap;
	}
	
	private List<StoreDTO> getReadyForFront(List<StoreDTO> stores){
		List<StatisticsDTO> statistics = storeMapper.getStatistics();
		Map<String, StatisticsDTO> statisticsMap = new HashMap<>();
		//튀는 값 없애고 다시 통계값 가져오기
		for(StatisticsDTO dto : statistics) {
			StatisticsDTO refinedOne = storeMapper.getRefinedStatistics(dto).getFirst();
			log.info("통계: {}", refinedOne);
			statisticsMap.put(refinedOne.getAreaNm(), refinedOne);
		}
						
		for(StoreDTO store : stores) {
			store.setMenuItems(getstoreMapper.getStoreMenu(store));
			store.setImgURLs(getstoreMapper.getStoreImg(store));
			//평균과 표준편차
			double meanOfPrice = statisticsMap.get(store.getAreaNm()).getMeanOfPrice();
			double stdOfPrice = statisticsMap.get(store.getAreaNm()).getStdOfPrice();
			double meanOfParty = statisticsMap.get(store.getAreaNm()).getMeanOfParty();
			double stdOfParty = statisticsMap.get(store.getAreaNm()).getStdOfParty();
			//치른 돈 정규화
			double bills = 2.5 + ((double)store.getTotalPrice() - meanOfPrice)/stdOfPrice;
			if(bills > 5.0)
				bills = 5.0;
			else if (bills < 0.0)
				bills = 0.0;

			//다녀간 발길 정규화	
			double feet = 2.5 + ((double)store.getTotalParty() - meanOfParty)/stdOfParty;
			if(feet > 5.0)
				feet = 5.0;
			else if(feet < 0.0)
				feet = 0.0;
					
			store.setBills(bills);
			store.setFeet(feet);
//			log.info("가게 정보: {}", store);
		}
		return stores;
	}	
	
}

