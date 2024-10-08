package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.app.service.NearStoreService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@Controller
@RequiredArgsConstructor
public class NearStoreController {
	
    private final NearStoreService nearStoreService;

    // 카카오 API 키 (application.properties 설정)
    @Value("${restApiKey.kakao}")
    private String apiKey;

    /**
     * 음식점 추천 List Post 요청
     * @param request 요청 데이터
     * @return ResponseEntity
     */
    // http://localhost:8080/nearStore?latitude=37.5291341&longitude=127.1215929&page=1
    @GetMapping("/nearStore")
    private ResponseEntity<String> searchLunch(HttpServletRequest request) {
//        String latitude = request.getParameter("latitude");
//        String longitude = request.getParameter("longitude");
    	String query = request.getParameter("q");
        String page = request.getParameter("page");

        return nearStoreService.getSearchStoreList(query, page, apiKey);

    }

    @PutMapping("/nearStore")
    private ResponseEntity<String> updateLunch() {
        return ResponseEntity.status(HttpStatus.OK).body("PutMapping!!");
    }

    @DeleteMapping("/nearStore")
    private ResponseEntity<String> deleteLunch() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("DeleteMapping!!");
    }

}