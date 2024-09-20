package com.app.service;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.app.dto.GsonDTO;
import com.app.dto.ItemDTO;
import com.app.dto.RawDataDTO;
import com.app.mapper.DataMapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataService {

	@Autowired
	private DataMapper dataMapper;
	
	public String insertData() {
		int index = 2;
		List<RawDataDTO> rawData = dataMapper.getRawData();
		log.info("마리아에서 가져온 것: {}", rawData.get(index).getAreaNm() + " " + rawData.get(index).getStoreNm());
		
		String dataFromNaver = naverSearchList(rawData.get(index).getAreaNm() + " " + rawData.get(index).getStoreNm());
		log.info("네이버에서 가져온 것: {}", dataFromNaver);
		
		Gson gson = new Gson();
		GsonDTO nData = gson.fromJson(dataFromNaver, GsonDTO.class);
		
		//네이버 데이터 5개 짜리 향상된 for문
//		log.info("실험: {}", nData.getItems().get(0).getTitle());
			for(ItemDTO nItem : nData.getItems()) {
				String category = nItem.getCategory().split(">")[0];
				System.out.println("나와라!:" + category);
				if(!"한식".equals(category)  && !"음식점".equals(category)) continue;
				if(!nItem.getAddress().contains(rawData.get(index).getAreaNm())) continue; //네이버 데이터 & rawData 내의 지역 불일치하면 다음으로 긔긔
			}
		
	
		
//		for(RawDataDTO rawDatum : rawData) {
//			ResponseEntity<String> dataFromNaver = naverSearchList(rawDatum.getAreaNm() + " " + rawDatum.getStoreNm());
//			log.info("네이버에서 가져온 것: {}", dataFromNaver);
//		
//			DataDTO ourData = new DataDTO();
//			
//			ourData.setTitle(null);
//			ourData.setLink(null);
//			ourData.setCategory(null);
//			ourData.setDescription(null);
//			ourData.setTelephone(null);
//			ourData.setAddress(null);
//			ourData.setRoadAddress(null);
//			ourData.setMapx(0);
//			ourData.setMapy(0);
//			
//			ourData.setPrice(rawDatum.getPrice());
//			ourData.setParty(rawDatum.getParty());
//			ourData.setVisitDate(rawDatum.getDate());
//			
//			dataMapper.setOwnData(ourData);
//		}
			return dataFromNaver;
	}
	
	private String naverSearchList(String text) {
		
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
	        
//	        String responseBody = responseEntity.getBody();
	        
	        return responseEntity.getBody();
		}
	
	
}
