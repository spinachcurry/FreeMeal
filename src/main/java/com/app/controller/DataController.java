package com.app.controller;

import java.net.URI;
import java.nio.charset.Charset;
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
import com.app.dto.crawling.KageDTO;
import com.app.mapper.DataMapper;
import com.app.service.DataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class DataController {

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
	        
	        return ResponseEntity.ok(responseBody);
		}

	@Autowired
	private DataMapper dataMapper;
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private WebCrawling webCrawling;

//	@GetMapping(value="/")
//	public String test() {
//		return dataService.insertData();
//	}

	@GetMapping("/crawling")
	public KageDTO crawling() {
		return webCrawling.getImageAndMenu();
	}
	
}
	

