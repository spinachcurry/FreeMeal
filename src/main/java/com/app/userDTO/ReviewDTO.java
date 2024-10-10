package com.app.userDTO;
 
import java.time.LocalDateTime;
import java.util.List;

import com.app.dto.StoreDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class ReviewDTO {
	
	private List<StoreDTO> store;
	private int reviewNo;                  // 리뷰 ID
    private String storeId;                // 스토어 ID
    private String userId;                 // 사용자 ID
    private String menuId;                 // 메뉴 ID (선택 사항)
    private int rating;                    // 평점 (선택 사항)
    private String content;                // 리뷰 내용 (선택 사항)
    private LocalDateTime createDate;      // 생성일자
    private LocalDateTime modifiedDate;    // 수정일자
    private String status;           // 리뷰 상태
	

}
