package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.service.DidsService; 
import com.app.userDTO.DidsDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")// React 클라이언트 주소 
public class DidsController {
	
	@Autowired
	private DidsService didsService;
	
	//찜하기 / 취소 토글toggleDibs
	@PostMapping("/toggleDibs")
	public ResponseEntity<String> toggleDibs(
	        @RequestParam("userId") String userId,
	        @RequestParam("address") String address,
	        @RequestParam("didStatus") int didStatus) {
	    System.out.println("Toggling dibs for userId: " + userId + ", address: " + address + ", status: " + didStatus);

	    try {
	        didsService.addDibs(userId, address, didStatus);
	        
	        if (didStatus == 1) {
	            return ResponseEntity.ok("찜하기가 성공적으로 추가되었습니다.");
	        } else if (didStatus == 0) {
	            return ResponseEntity.ok("찜하기가 해제되었습니다.");
	        } else {
	            return ResponseEntity.badRequest().body("잘못된 상태 값입니다.");
	        }

	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body("잘못된 상태 값입니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("오류가 발생했습니다: " + e.getMessage());
	    }
	}

    //찜하기 확인용
    @GetMapping("/didCheck")
    public ResponseEntity<String> checkDibs(
            @RequestParam("userId") String userId,
            @RequestParam("address") String address) {
        System.out.println("Checking dibs status for userId: " + userId + ", address: " + address);

        try {
            List<DidsDTO> didStatusList = didsService.selectDibs(userId, address);

            if (!didStatusList.isEmpty()) {
                return ResponseEntity.ok("이미 찜한 상태입니다.");
            } else {
                return ResponseEntity.ok("찜한 상태가 아닙니다.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류가 발생했습니다: " + e.getMessage());
        }
    }
    //찜 카운트
    @GetMapping("/count")
    public ResponseEntity<Integer> getDibsCount(@RequestParam("address") String address) {
    	
    	int count = didsService.getDibsCount(address);
        try { 
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
  //찜하기 목록 불러오기
    @GetMapping("/getDibsByUserId")
    public ResponseEntity<List<DidsDTO>> getDibsByUserId(@RequestParam("userId") String userId) {
        try {
            List<DidsDTO> dibsList = didsService.getDibsByUserId(userId);
            if (dibsList.isEmpty()) {
                return ResponseEntity.noContent().build();  // 데이터가 없을 때
            }
            return ResponseEntity.ok(dibsList);  // 성공적으로 데이터를 가져왔을 때
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();  // 오류가 발생했을 때
        }
    }
 
}
