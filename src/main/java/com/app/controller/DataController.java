package com.app.controller;

import java.net.URI;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.app.component.WebCrawling;

import com.app.mapper.DataMapper;
import com.app.service.DataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class DataController {

	@Autowired
	private DataMapper dataMapper;
	
	 @GetMapping(value = "/search", produces = "application/json; charset=UTF-8")
	 public ResponseEntity<String> naverSearchList(@RequestParam("text") String text) {
		
	 	String clientId = "Mybp3tJ8oOogHiifoV6Y";
	 	String clientSecret = "mntjlH4J1B";
	 	URI uri = UriComponentsBuilder
	       		  .fromUriString("https://openapi.naver.com")
	       		  .path("/v1/search/local.json")
	       		  .queryParam("query", text)
	       		  .queryParam("display", 5)
	       		  .queryParam("start", 1)
	       		  .queryParam("sort", "random")
	               .encode(Charset.forName("UTF-8"))
	       		  .build()
	               .toUri();
		 
	         RequestEntity<Void> req = RequestEntity
	                 .get(uri)
	                 .header("X-Naver-Client-Id", clientId)
	                 .header("X-Naver-Client-Secret", clientSecret)
	                 .header("Content-Type", "application/json; charset=UTF-8")
	                 .build();
	        
	         RestTemplate restTemplate = new RestTemplate();
	        
	         ResponseEntity<String> responseEntity = restTemplate.exchange(req, String.class);
	        
	         String responseBody = responseEntity.getBody();
	         
	         int callCount; //지금까지 카운팅 된 수로 시작점 지정 >> 밖에다 적어 놓기로! >> database에 기록해두는것.
	 		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
	 		Date now = new Date();
	 		String toDay = format.format(now);
	 		Map<String, Object> map = new HashMap<>();
	 		log.info("오늘 날짜: {}", toDay);
	 		
//	 		log.info(" : {}", dataMapper.getCount(toDay));
	 		if(!dataMapper.getCount(toDay).isEmpty()) {
	 			log.info("처음이야?");
	 			callCount = dataMapper.getCount(toDay).get(0);
	 		}else{
	 			log.info("처음이다 왜?");
	 			callCount = 0;
	 		}
	 		callCount++;
	 		map.put("countdate", toDay);
			map.put("count", callCount);
			log.info("확인용: {}", map);
			dataMapper.setCount(map);
	         
	         return ResponseEntity.ok(responseBody);
	 	}
	
	@Autowired
	private DataService dataService;

	@GetMapping(value="/getData")
	public String test() {
		return dataService.insertData();
	}
	
	@Autowired
	private WebCrawling webCrawling;
	
	@GetMapping("/crawling")
	public String crawling() {
		//webCrawling.process(StoreDTO.builder().areaNm("강동구").title("24시 카페 주디자인").build());
		
		dataService.insertMenuAndImge();
		return "메뉴, 사진 네이버에서 따오기";
	}
	
//	@Autowired
//	private  StoreMapper storeMapper;
	
//	@GetMapping("/test")
//	public List<StoreDTO> test(@RequestParam Map<String, Object> paramMap) {
//		return storeMapper.test1(paramMap);
//	}
	
}

