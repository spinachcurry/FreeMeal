package com.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.StoreDTO;
import com.app.mapper.StoreMapper;
import com.app.service.StoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StoreController {

	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private StoreService storeService;

	// 전체 가게 목록 (예시, 필요에 따라 제거 가능)
	@GetMapping(value="/storeList")
	public List<StoreDTO> storeList() {
		return storeMapper.storeList();
	}

	// 가게 상세 정보
	@GetMapping("/storeDetail")
	public StoreDTO storeDetail(@RequestParam("store") String title) {
		return storeService.storeDetail(title);
	}

	// 메인 페이지에서 지역, 가격, 방문별 추천 가게 목록
	@PostMapping("/storeNearby")
	public Map<String, List<StoreDTO>> storeNearby(@RequestBody Map<String, Object> location) {
		log.info("Received location: {}", location);
		log.info("Longitude: {}", location.get("longitude"));
		log.info("Latitude: {}", location.get("latitude"));
		
		// 예시로 생성된 데이터에 대한 로그를 추가
		Map<String, List<StoreDTO>> result = storeService.storeNearby(location);
		log.info("Returned Data: {}", result);
		
		return result;
	}


	
	
}

	
