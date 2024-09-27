package com.app.service;

import java.net.URI;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.app.dto.DataDTO;
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
	//우선 인덱스 하나로 데이터 가져오는지 혹은 없는지 확인 여부
	public final int MaxCount = 25000;
	public String insertData() {
		List<RawDataDTO> rawData = dataMapper.getRawData();
		if(rawData.size() < 1) {
			return "새로운 데이터가 없습니다.";
		}
		//for(int index = 0; index < rawData.size(); index++) {		
		int callCount; //지금까지 카운팅 된 수로 시작점 지정 >> 밖에다 적어 놓기로! >> database에 기록해두는것.
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
		Date now = new Date();
		String toDay = format.format(now);
		Map<String, Object> map = new HashMap<>();
		log.info("오늘 날짜: {}", toDay);
		
//		log.info(" : {}", dataMapper.getCount(toDay));
		if(!dataMapper.getCount(toDay).isEmpty()) {
			log.info("처음이야?");
			callCount = dataMapper.getCount(toDay).get(0);
		}else{
			log.info("처음이다 왜?");
			callCount = 0;
		}
		try {
			for(RawDataDTO getOne : rawData ) {
				dataMapper.check(getOne.getNo());
				TimeUnit.MILLISECONDS.sleep(100);
				if(callCount >= MaxCount) {
					log.info("멈출 때가 됐다");
					map.put("countdate", toDay);
					map.put("count", callCount);
					dataMapper.setCount(map);
					return "오늘 25,000번 다 혔슈. 어따 되다 되야"; // 중간점검: 25000개를 다 사용했을 때! >> 강종
				}
				String dataFromNaver = naverSearchList(getOne.getAreaNm() + " " + getOne.getStoreNm());
//				log.info("네이버에서 가져온 것: {}", dataFromNaver);
				callCount++;
					 	
				Gson gson = new Gson();
				GsonDTO nData = gson.fromJson(dataFromNaver, GsonDTO.class);
				
				//네이버 데이터 5개 짜리 향상된 for문		
				DataDTO ourData = null;
				
				for(ItemDTO nItem : nData.getItems()) {
					String category = nItem.getCategory().split(">")[0];
					if(!"한식".equals(category)  && !"음식점".equals(category)) continue;
					if(!nItem.getAddress().contains(getOne.getAreaNm())) continue; //네이버 데이터 & rawData 내의 지역 불일치하면 다음으로 긔긔
			
					//dto 빌드 >> for 문 멈추기!
					ourData = DataDTO.builder()
							.title(nItem.getTitle().replace("<b>", "").replace("</b>", ""))
							.link(nItem.getLink())
							.category(nItem.getCategory())
							.description(nItem.getDescription())
							.telephone(nItem.getTelephone())
							.address(nItem.getAddress())
							.roadAddress(nItem.getRoadAddress())
							.mapx(nItem.getMapx())
							.mapy(nItem.getMapy())
							//raw data 에서 온 항목
							.price(getOne.getPrice())
							.party(getOne.getParty())
							.visitDate(getOne.getDate())
							.areaNm(getOne.getAreaNm())
							.build();
					break;
				}
				log.info("확인하며 공부하기^^ : {}", ourData);
				
		//			값이 null 이면 insert 안하도록 만들어보기
		//			① 배열에 저장된 값의 타입과 반복문에서 사용할 변수명 : 배열객체 이름
				if(ourData != null) {
					if(dataMapper.setOwnData(ourData) > 0)
						log.info("데이터 입력: {}", ourData);
					else
						log.info("데이터 안 입력!: {}" , ourData);
				}
				else log.info("널뛴다! : {}", ourData);
			}
			
			//카운팅은 이미 종료! >>
			//update 뭘 업데이트 하냐? 얼마나 카운팅 됐는지 날짜별 업뎃!
			//>> 항상 '오늘'날짜로 업데이트 하는거지!
			/**********************내일은 여기다****************/
			map.put("countdate", toDay);
			map.put("count", callCount);
			log.info("확인용: {}", map);
			dataMapper.setCount(map);	
			
			return "오늘 " + callCount + "번까지 무탈히 잘 끝났슈.";
		}
		 catch (InterruptedException e) {
			return "오류 났슈.";
		}
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