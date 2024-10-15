package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.mapper.ReviewMapper; 
import com.app.userDTO.DidsDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DidsService {
	 
	@Autowired
	private ReviewMapper reviewMapper;
	
	//찜하기
	public int addDibs(String userId, String address, int didStatus) {
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

    //찜하기 이미 찜했을 때
    public List<DidsDTO> selectDibs(String userId, String address) {
        List<DidsDTO> didStatusList = reviewMapper.selectDibs(userId, address);
        
        if (!didStatusList.isEmpty()) {
            DidsDTO didStatus = didStatusList.get(0);
            if (didStatus.getStatus() == 1) {
                return didStatusList;
            }
        }
        return new ArrayList<>(); // 또는 Optional.empty() 사용 가능
    }

    //찜 카운트
    public int getDibsCount(String address) {
        return reviewMapper.countDibs(address);
    }
    
    //찜 목록가져오기
    public List<DidsDTO> getDibsByUserId(String userId) {
        return reviewMapper.findDibsByUserId(userId);
    }

}
