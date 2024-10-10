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

	//전체 가게 목록
	@GetMapping(value="/storeList")
	public List<StoreDTO> storeList() {
		return storeMapper.storeList();
	}
	
	//가게 상세 페이지 >> 프론트에서 돌려주면 보여주는 것!
	@GetMapping("/storeDetail")
	public StoreDTO storeDetail(@RequestParam("store") String title) {
		return storeService.storeDetail(title);
	}
	
	@PostMapping("/storeNearby")
	public String storeNearby(@RequestBody Map<String, Object> location) {
		log.info("location: {}", location);
		return "성공이다!"; //storeService.storeNearby(location);
	}
	
		
}

	

