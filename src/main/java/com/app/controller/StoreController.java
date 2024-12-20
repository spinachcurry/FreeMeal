package com.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class StoreController {

	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private StoreService storeService;

	//전체 가게 목록
	@GetMapping("/storeList")
	public List<StoreDTO> storeList() {
		return storeMapper.storeList();
	}

	//가게 상세 페이지 >> 프론트에서 돌려주면 보여주는 것!
	@PostMapping("/storeDetail")
	public StoreDTO storeDetail(@RequestBody Map<String, Object> map) {
		log.info("이름: {}", map);
		return storeService.storeDetail(map);
	}
	
	//메인 페이지에 보여줄 3개의 大카테고리(지역별,가격별,방문별)
	@PostMapping("/storeNearby")
	public Map<String, List<StoreDTO>> storeNearby(@RequestBody Map<String, Object> location) {
		log.info("location: {}", location);
		return storeService.storeNearby(location);
	}
	
	//메인 페이지에서 전체 소팅 후 검색 기능
	@PostMapping("/searchStore")
	public Map<String, Object> searchStore(@RequestBody Map<String, Object> keykeyword) {
		log.info("keyword: {}", keykeyword);
		return storeService.searchStore(keykeyword);
	}
	
	
}

