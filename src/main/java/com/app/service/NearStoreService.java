package com.app.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NearStoreService {

    /**
     * 좌표 기준 음식점 데이터 조회
     * 파라미터 순서는 x, y 순서로 넘겨줘야 데이터 리턴 받을 수 있음
     * @param longitude 경도 -> x
     * @param latitude 위도 -> y
     * @param page 페이지 수 1,2,3
     * @param size 페이지당 조회 수 15
     * @return ResponseEntity
     */
    public ResponseEntity<String> getSearchStoreList(String query, String page, String restApiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + restApiKey);


        HttpEntity<String> entity = new HttpEntity<>("", headers);
//"https://dapi.kakao.com/v2/local/search/keyword.json"
//        "https://dapi.kakao.com/v2/local/search/category.json?"
//        String q = "서울 강서구 마곡동";
        String baseUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?" +
                "query=" + query +
                "&category_group_code=FD6" +
                "&page=" + page +
                "&size=15" ;
//                "&sort=distance" ;
//                "&x=" + latitude +
//                "&y=" + longitude +
//                "&radius=1" ;

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class);
    }


}
