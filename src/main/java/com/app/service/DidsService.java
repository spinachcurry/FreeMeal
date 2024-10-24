package com.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.dto.DidsDTO;
import com.app.mapper.ReviewMapper;
 
@Service 
public class DidsService {
	 
	@Autowired
	private ReviewMapper reviewMapper;
	
	  // 전체 요청 핸들링
	public ResponseEntity<?> handleDibs(Map<String, Object> requestBody) {
        // Map에서 값 추출
        String action = (String) requestBody.get("action");
        String userId = (String) requestBody.get("userId");
        String address = (String) requestBody.get("address");
        String storeNm = (String) requestBody.get("storeNm");
        Integer didStatus = requestBody.get("didStatus") != null ? (Integer) requestBody.get("didStatus") : null;

        // action 값에 따라 다른 로직을 처리
        switch (action) {
            case "toggle":
                return toggleDibs(userId, address, didStatus);
            case "check":
                return checkDibs(userId, address);
            case "count":
                return getDibsCount(address);
            case "list":
                return getDibsByUserId(userId);
            case "menu":  // 메뉴 조회 액션 처리
                return getMenuByAddress(address);
            case "img":  // 메뉴 조회 액션 처리
                return getMenuByAddress(storeNm);
            default:
                return ResponseEntity.badRequest().body("Invalid action specified");
        }
    }
	//메뉴판 이미지
	 public ResponseEntity<List<DidsDTO>> getMenuimg(String storeNm) {
	        List<DidsDTO> menu = reviewMapper.menuImg(storeNm); 
	        if (menu.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(menu);
	        } 
	        return ResponseEntity.ok(menu);
	    }
	//디테일 메뉴판 불러오기 
    public ResponseEntity<List<DidsDTO>> getMenuByAddress(String address) {
        List<DidsDTO> menu = reviewMapper.oneMenu(address);

        // 데이터가 존재하지 않으면 404 Not Found 반환
        if (menu.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(menu);
        }

        // 데이터가 있으면 200 OK와 함께 반환
        return ResponseEntity.ok(menu);
    }
    // 찜하기 / 취소 토글
    private ResponseEntity<String> toggleDibs(String userId, String address, int didStatus) {
        try {
            addDibs(userId, address, didStatus);
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
    // 찜 추가 / 삭제 로직
    private int addDibs(String userId, String address, int didStatus) {
        if (didStatus == 1) {
            // Add dibs
            System.out.println("Adding dibs for userId: " + userId + ", address: " + address);
            return reviewMapper.insertDibs(userId, address, 1);
        } else if (didStatus == 0) {
            // Remove dibs
            System.out.println("Removing dibs for userId: " + userId + ", address: " + address);
            return reviewMapper.insertDibs(userId, address, 0); // 삭제하는 로직 추가
        } else {
            throw new IllegalArgumentException("Invalid dibs status value: " + didStatus);
        }
    } 
    // 찜 상태 확인  
    private ResponseEntity<String> checkDibs(String userId, String address) {
        List<DidsDTO> didStatusList = reviewMapper.selectDibs(userId, address);
        int status = !didStatusList.isEmpty() ? 1 : 0; // 1: 찜 상태, 0: 찜 상태 아님
        return ResponseEntity.ok(String.valueOf(status));  // Integer를 String으로 변환하여 반환
    } 
    // 찜 카운트
    private ResponseEntity<String> getDibsCount(String address) {
        try {
            int count = reviewMapper.countDibs(address);
            return ResponseEntity.ok(String.valueOf(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다: " + e.getMessage());
        }
    } 
    // 유저별 찜 목록 불러오기 
    public ResponseEntity<List<DidsDTO>> getDibsByUserId(String userId) {
      try {
          List<DidsDTO> dibsList = reviewMapper.findDibsByUserId(userId);
          return dibsList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dibsList);
      } catch (Exception e) {
          return ResponseEntity.internalServerError().build();
      }
  } 
  
}
