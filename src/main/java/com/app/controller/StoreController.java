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
	
	//메인 페이지에 보여줄 3개의 大카테고리(지역별,가격별,방문별)
	@PostMapping("/storeNearby")
	public Map<String, List<StoreDTO>> storeNearby(@RequestBody Map<String, Object> location) {
		log.info("location: {}", location);
		return storeService.storeNearby(location);
	}
	
	//메인 페이지에서 전체 소팅 후 검색 기능 >> 프론트 그려야함
	@PostMapping("/searchStore")
	public List<StoreDTO> searchStore(@RequestBody Map<String, Object> keykeyword) {
		log.info("keyword: {}", keykeyword);
		return storeService.searchStore(keykeyword);
	}
	
	//해당 음식점 고유 title , areaNm 으로 찾아서 가게가 가지고 있는 link만 가져오도록 >> 프론트로 돌려주기
//	@PostMapping("/storeLink")
//	public List<StoreDTO> storeTitle(@RequestParam Map<String, Object> storeinfo) {
//		log.info("storeTitle:{}", storeinfo);
//		return storeService.storeLink(storeinfo);
//	}
//	
	
}

