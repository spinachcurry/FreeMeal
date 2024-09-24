package com.app.service;

import java.net.URI;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
	private final int MaxCount = 25000;
	public void insertData() {
		List<RawDataDTO> rawData = dataMapper.getRawData();
//		log.info("마리아에서 가져온 것: {}", rawData.get(index).getAreaNm() + " " + rawData.get(index).getStoreNm());
		
		//for(int index = 0; index < rawData.size(); index++) {		
		int callCount; //지금까지 카운팅 된 수로 시작점 지정 >> 밖에다 적어 놓기로! >> database에 기록해두는것.
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
		Date now = new Date();
		String currentDate = format.format(now); 
		
		if(!dataMapper.getCount(currentDate).isEmpty()) {
			log.info("처음이야?");
			callCount = dataMapper.getCount(currentDate).getFirst();
		}else{
			log.info("처음이다 왜?");
			callCount = 0;
		}
		try {
			for(RawDataDTO getOne : rawData ) {
				TimeUnit.MILLISECONDS.sleep(100);
				if(callCount >= MaxCount) {
					log.info("멈출 때가 됐다");
					break; // 중간점검: 25000개를 다 사용했을 때! >> 강종

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
					System.out.println("나와라!:" + category);
					if(!"한식".equals(category)  && !"음식점".equals(category)) continue;
					if(!nItem.getAddress().contains(getOne.getAreaNm())) continue; //네이버 데이터 & rawData 내의 지역 불일치하면 다음으로 긔긔
			
					//dto 빌드 >> for 문 멈추기! >> 오로지 신라 삼계탕만을 위한 for문
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
							
							.price(getOne.getPrice())
							.party(getOne.getParty())
							.visitDate(getOne.getDate())
							.build();
					break;
				}
				log.info("확인하며 공부하기^^ : {}", ourData);
				
				//카운팅 다 끝났을 때! 종료 지점 >> update 0925 할 일
				/**********************내일은 여기다***********************/
				
				
				
		//			값이 null 이면 insert 안하도록 만들어보기
		//			① 배열에 저장된 값의 타입과 반복문에서 사용할 변수명 : 배열객체 이름
				if(ourData != null) {
					if(dataMapper.setOwnData(ourData) > 0)
						System.out.println("데이터 입력!");
					else
						System.out.println("데이터 안 입력!");
				}
				else System.out.println("널뛴다!!");
				
			}
		}
		 catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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