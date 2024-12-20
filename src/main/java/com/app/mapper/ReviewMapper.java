package com.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.app.dto.DidsDTO;
import com.app.dto.ReviewDTO; 

@Mapper
public interface ReviewMapper {
	
	//리뷰(유저용)신고미포
	@Select( " SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, "
			+ "	re.reviewNo, re.userId, re.content, re.modifiedDate, re.status "
			+ "	FROM freemeal AS tf "
			+ "	INNER JOIN Reviews AS re  "
			+ "	ON tf.address = re.address  "
			+ "	WHERE re.userId = #{userId} AND re.STATUS ='일반' AND re.del = 0"
			+ "	GROUP BY tf.title, tf.address, tf.category, re.userId, re.content, re.modifiedDate, re.status "
			+ "	ORDER BY re.modifiedDate DESC;")
	List<ReviewDTO> findReviewsByStatus(@Param("userId") String userId); 
	//리뷰(관리자)신고만
	@Select( " SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, "
			+ "	re.reviewNo, re.userId, re.content, re.modifiedDate, re.status "
			+ "	FROM freemeal AS tf "
			+ "	INNER JOIN Reviews AS re  "
			+ "	ON tf.address = re.address  "
			+ "	WHERE re.userId = #{userId} or re.status='신고' "
			+ "	GROUP BY tf.title, tf.address, tf.category, re.userId, re.content, re.modifiedDate, re.status "
			+ "	ORDER BY re.modifiedDate DESC;")
	List<ReviewDTO> getReviewsStatus(@Param("userId") String userId); 
	//리뷰 수정
	@Update("UPDATE Reviews " 
			+ "SET address = #{address}, userId=#{userId}, content = #{content}, modifiedDate = NOW(), status='일반' " 
			+ "WHERE reviewNo = #{reviewNo} ")
	int updateReview(ReviewDTO reviewDTO);
	//리뷰 삭제
	@Update("UPDATE Reviews " 
			+ "SET del = 1 " 
			+ "WHERE reviewNo = ${reviewNo} ")
	int deleteReview(int reviewNo);
	//////가게 상세 페이지리뷰 불러오기(리뷰용)
	@Select("	SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, "
			+ "	re.reviewNo, re.userId, re.content, re.modifiedDate,re.createDate, re.status "
			+ "	FROM freemeal AS tf "
			+ "	INNER JOIN Reviews AS re "
			+ "	ON tf.address = re.address "
			+ "	WHERE tf.address = #{address} AND re.STATUS ='일반' AND re.del=0"
			+ "	GROUP BY tf.title, tf.address, tf.category, re.userId, re.content, re.modifiedDate, re.status"
			+ " ORDER BY re.modifiedDate DESC"
			+ " LIMIT ${offset}, ${size}")
	List<ReviewDTO> FindStoreOne(Map<String, Object> params);
	//리뷰쓰기
	@Insert("INSERT INTO Reviews (address, userId, content, createDate, modifiedDate) " +
	        "VALUES (#{address}, #{userId}, #{content}, NOW(), NOW())")
	int insertReview(ReviewDTO reviews); 
	//신고버튼
	@Update("UPDATE Reviews SET STATUS = '신고' WHERE reviewNo =#{reviewNo} ")
	int updateReport(ReviewDTO reviewNo);
	//찜하기 ///////////////////////////////////////////////////////
	 @Insert(" INSERT INTO Dibs (userId, address, status) " 
	        + " VALUES (#{userId}, #{address}, #{status}) " 
	        + " ON DUPLICATE KEY UPDATE status = #{status} ")
	int insertDibs(@Param("userId") String userId, @Param("address") String address, @Param("status") int status);
	//찜을 했을 때
	@Select("SELECT * FROM Dibs WHERE userId=#{userId} and address =#{address} and  STATUS = '1' ")
	List<DidsDTO> selectDibs(@Param("userId") String userId, @Param("address") String address) ; 
	//찜 카운트
	@Select("SELECT COUNT(userId) AS COUNT " 
			+ "FROM Dibs " 
			+ "WHERE STATUS = '1' AND address = #{address}")
	int countDibs(@Param("address") String address);
	//찜목록 불러오기;
	@Select ("SELECT  tf.title, tf.areaNm, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, did.* "
			+ "	FROM freemeal AS tf "
			+ "	INNER JOIN Dibs AS did "
			+ "	ON tf.address = did.address "
			+ "	WHERE did.STATUS = '1' AND did.userId =#{userId} "
			+ "	GROUP BY tf.title, tf.address, tf.category" )
	List<DidsDTO> findDibsByUserId(@Param("userId") String userId);
	//메뉴판
	@Select (" SELECT sm.price, sm.name, tf.title, tf.address, tf.category, tf.lng, tf.lat,tf.roadAddress "
			+ "FROM freemeal AS tf "
			+ "INNER JOIN store_menu AS sm ON sm.storeNm = tf.title "
			+ "WHERE tf.address = #{address}"
			+ "GROUP BY sm.name "
			+ "LIMIT 7 " )
	List<DidsDTO> OneMenu(@Param("address") String address);
	
}
