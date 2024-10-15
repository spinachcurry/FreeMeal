package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.service.LoginService;
import com.app.service.ReviewService;
import com.app.userDTO.ReviewDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")// React 클라이언트 주소 
public class ReviewControllre {
	
	@Autowired
	private ReviewService reviewService;
	
	   //리뷰 불러오기Review
	@GetMapping("/getReviewsByStatus")
	public ResponseEntity<?> getReviewsByStatus(@RequestParam("userId") String userId,
	                                            @RequestParam("status") String status) {
	    try {
	        List<ReviewDTO> reviews = reviewService.getReviewsByStatusAndRole(userId, status);

	        if (reviews == null || reviews.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰가 없습니다.");
	        }
	        return ResponseEntity.ok(reviews);

	    } catch (IllegalArgumentException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰를 가져오는 중 서버 오류가 발생했습니다.");
	    }
	}

    // 리뷰 수정 요청 처리
    @PostMapping("/updateReview")
    public ResponseEntity<?> updateReview(@RequestBody ReviewDTO reviewDTO) { 
        try {
            // 리뷰 업데이트 수행
            boolean isUpdated = reviewService.updateReview(reviewDTO);

            if (isUpdated) {
                // 업데이트 성공
                return ResponseEntity.ok().body("리뷰가 성공적으로 수정되었습니다.");
            } else {
                // 업데이트 실패 (예: 해당 리뷰가 존재하지 않는 경우)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            // 예외 처리 및 오류 응답
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("리뷰를 수정하는 중 오류가 발생했습니다.");
        }
    }

    // 리뷰 목록을 반환하는 GET 엔드포인트
    @GetMapping("/getReviews")
    public ResponseEntity<List<ReviewDTO>> getReviews(@RequestParam("address") String address) {
        List<ReviewDTO> reviews = reviewService.getAllReviews(address); 
        
        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(reviews);
    }
    // 새로운 리뷰를 추가하는 POST 엔드포인트
    @PostMapping("/addReview")
    public ResponseEntity<String> addReview(@RequestBody ReviewDTO review) {
        try {
            int insertedCount = reviewService.addReview(review);
            if (insertedCount > 0) { 
                return ResponseEntity.ok("리뷰가 성공적으로 추가되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("리뷰 추가에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰를 추가하는 중 오류가 발생했습니다.");
        }
    }
 // 리뷰 수정 요청 처리
    @PostMapping("/report")
    public ResponseEntity<String> reportReview(@RequestBody ReviewDTO reviewNo) {
        try {
            int result = reviewService.updateReport(reviewNo);
            
            if (result > 0) {
                return ResponseEntity.ok("리뷰가 성공적으로 신고되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리뷰 신고에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 신고 처리 중 오류가 발생했습니다.");
        }
    }
}
