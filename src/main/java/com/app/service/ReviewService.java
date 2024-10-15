package com.app.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.mapper.ReviewMapper; 
import com.app.userDTO.ReviewDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
	   //리뷰받아오기 유저 
		 public List<ReviewDTO> getReviewsByStatus(String userId) {
		        return reviewMapper.findReviewsByStatus(userId);
		    }
		//리뷰받아오기 관리자
	    public List<ReviewDTO> getReviewsStatus(String userId) {
	        return reviewMapper.getReviewsStatus(userId);
	    }
	    //개인 리뷰 업데이트하기
	    public boolean updateReview(ReviewDTO reviewDTO) {
	        int updatedRows = reviewMapper.updateReview(reviewDTO);
	        return updatedRows > 0;
	    } 
	    //가게 상세리뷰 받아오기 
	    public List<ReviewDTO> getAllReviews(@Param("address") String address) {
	        return reviewMapper.FindStoreOne(address);
	    }  
	    // 리뷰 작성(상점) 
	    public int addReview(ReviewDTO review) {
	        return reviewMapper.insertReview(review);
	    } 
	    //리뷰신고
	    public int updateReport(ReviewDTO reviewNo) {
	        return reviewMapper.updateReport(reviewNo);
	    }
	  

}
