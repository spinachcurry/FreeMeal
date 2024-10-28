package com.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.dto.ReviewDTO;
import com.app.mapper.ReviewMapper; 

@Service 
public class ReviewService {
	
	@Autowired
	private ReviewMapper reviewMapper;
	//리뷰 받아오기 유저 or 관리자 분리
		public List<ReviewDTO> getReviewsByStatusAndRole(String userId, String status) {
		    if ("1".equals(status) || "2".equals(status)) {
		        return getReviewsByStatus(userId); // 유저 리뷰 조회
		    } else if ("3".equals(status)) {
		        return getReviewsStatus(userId); // 관리자 리뷰 조회
		    } else {
		        throw new IllegalArgumentException("유효하지 않은 status 값입니다.");
		    }
		}  
		public ResponseEntity<?> handleReviewAction(Map<String, Object> requestBody) {
		    String action = (String) requestBody.get("action");
		    String userId = (String) requestBody.get("userId");
		    String status = (String) requestBody.get("status");

		    // action 값에 따른 분기 처리
		    if ("getReviews".equals(action)) {
		        return handleGetReviews(userId, status);
		    } else if ("updateReview".equals(action)) {
		        ReviewDTO reviewDTO = new ReviewDTO();
		        reviewDTO.setReviewNo((Integer) requestBody.get("reviewNo"));
		        reviewDTO.setUserId((String) requestBody.get("userId"));
		        reviewDTO.setAddress((String) requestBody.get("address"));
		        reviewDTO.setContent((String) requestBody.get("content"));
		        reviewDTO.setRating((Integer) requestBody.get("rating"));
		        return updateReview(reviewDTO);
		    } else if ("getStoreReviews".equals(action)) {
		        // 가게 상세리뷰 받아오기
		    	Map<String, Object> reviewMap = new HashMap<>();
		    	reviewMap.put("offset", requestBody.get("offset"));
		    	reviewMap.put("size", requestBody.get("size"));
		    	reviewMap.put("address", requestBody.get("address"));
		        return getAllReviews(reviewMap);
		    } else if ("addReview".equals(action)) {
		        // 리뷰 작성
		        ReviewDTO reviewDTO = new ReviewDTO();
		        reviewDTO.setUserId((String) requestBody.get("userId"));
		        reviewDTO.setAddress((String) requestBody.get("address"));
		        reviewDTO.setContent((String) requestBody.get("content"));
		        return ResponseEntity.ok(addReview(reviewDTO));
		    } else if ("reportReview".equals(action)) {
		        // 리뷰 신고
		        ReviewDTO reviewDTO = new ReviewDTO();
		        reviewDTO.setReviewNo((Integer) requestBody.get("reviewNo"));
		        return ResponseEntity.ok(updateReport(reviewDTO));
		    } else {
		        return ResponseEntity.badRequest().body("Invalid action");
		    }
		} 
		// 리뷰 조회 로직
		private ResponseEntity<?> handleGetReviews(String userId, String status) {
		    try {
		        List<ReviewDTO> reviews = getReviewsByStatusAndRole(userId, status);  // status에 따라 리뷰 조회
		        return ResponseEntity.ok(reviews);
		    } catch (IllegalArgumentException e) {
		        return ResponseEntity.badRequest().body(e.getMessage());
		    }
		}  
		    // 리뷰 조회 메서드
		    public List<ReviewDTO> getReviewsByStatus(String userId) {
		        return reviewMapper.findReviewsByStatus(userId); // 기존 매퍼 호출
		    } 
		    // 리뷰 수정 메서드
		    public ResponseEntity<?> updateReview(ReviewDTO reviewDTO) {
		        try {
		            int updatedRows = reviewMapper.updateReview(reviewDTO);
		            
		            if (updatedRows > 0) {
		                return ResponseEntity.ok().body("리뷰가 성공적으로 수정되었습니다.");
		            } else {
		                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰를 찾을 수 없습니다.");
		            }
		        } catch (Exception e) {
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                    .body("리뷰를 수정하는 중 오류가 발생했습니다.");
		        }
		    } 
		//리뷰받아오기 관리자
	    public List<ReviewDTO> getReviewsStatus(String userId) {
	        return reviewMapper.getReviewsStatus(userId);
	    }
	    //가게 상세리뷰 받아오기 
	    public ResponseEntity<Map<String, Object>> getAllReviews(Map<String, Object> reviewMap) {
//	        List<ReviewDTO> reviews = reviewMapper.FindStoreOne(reviewMap);
//	        if (reviews.isEmpty()) {
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//	        }
	        Map<String, Object> reviews = new HashMap<>();
	        reviews.put("offset", Integer.parseInt(String.valueOf(reviewMap.get("offset")) + Integer.parseInt(String.valueOf(reviewMap.get("size")))));
	        reviews.put("reviews", reviewMapper.FindStoreOne(reviewMap));
	        return ResponseEntity.ok(reviews);
	    }   
	    // 리뷰 작성(상점) 
	    public int addReview(ReviewDTO review) {
	        try {
	            // 리뷰를 데이터베이스에 삽입
	            int result = reviewMapper.insertReview(review); 
	            // 삽입된 행이 없을 경우 (0이면 실패)
	            if (result <= 0) {
	                throw new Exception("리뷰 삽입에 실패했습니다.");
	            } 
	            // 정상적으로 삽입된 경우
	            return result; 
	        } catch (IllegalArgumentException e) {
	            // 입력 값에 문제가 있는 경우
	            System.err.println("잘못된 인자: " + e.getMessage());
	            throw e;  // 필요한 경우 상위 계층으로 예외를 전달 
	        } catch (Exception e) {
	            // 그 외 발생한 예외 처리
	            System.err.println("리뷰 추가 중 오류 발생: " + e.getMessage());
	            throw new RuntimeException("리뷰 추가 중 오류가 발생했습니다.", e);  // 예외를 다시 던짐
	        }
	    }
	    //리뷰신고
	    public int updateReport(ReviewDTO reviewNo) {
	        return reviewMapper.updateReport(reviewNo);
	    }   
} 
