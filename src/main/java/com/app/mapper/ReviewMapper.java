package com.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.app.userDTO.DidsDTO;
import com.app.userDTO.ReviewDTO; 

@Mapper
public interface ReviewMapper {
	
	//리뷰(유저용)신고미포
	@Select( " SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, "
			+ "	re.reviewNo, re.userId, re.content, re.modifiedDate, re.status "
			+ "	FROM freemeal AS tf "
			+ "	INNER JOIN Reviews AS re  "
			+ "	ON tf.address = re.address  "
			+ "	WHERE re.userId = #{userId} AND re.STATUS ='일반' "
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
	//////가게 상세 페이지리뷰 불러오기(리뷰용)
	@Select("	SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, "
			+ "	re.reviewNo, re.userId, re.content, re.modifiedDate,re.createDate, re.status "
			+ "	FROM freemeal AS tf "
			+ "	INNER JOIN Reviews AS re "
			+ "	ON tf.address = re.address "
			+ "	WHERE tf.address = #{address} AND STATUS ='일반'"
			+ "	GROUP BY tf.title, tf.address, tf.category, re.userId, re.content, re.modifiedDate, re.status"
			+ " ORDER BY re.modifiedDate DESC; ")
	List<ReviewDTO> FindStoreOne(@Param("address") String address);
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
	@Select("SELECT * FROM Dibs "
			+ "WHERE userId=#{userId} and address =#{address} and  STATUS = '1' ")
	List<DidsDTO> selectDibs(@Param("userId") String userId, @Param("address") String address) ;
	//찜 카운트
	@Select("SELECT COUNT(userId) AS COUNT " 
			+ "FROM Dibs " 
			+ "WHERE STATUS = '1' AND address = #{address}")
	int countDibs(@Param("address") String address);
	//찜목록 불러오기;
	@Select ("SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, did.* "
			+ "	FROM freemeal AS tf "
			+ "	INNER JOIN Dibs AS did "
			+ "	ON tf.address = did.address "
			+ "	WHERE did.STATUS = '1' AND did.userId =#{userId} "
			+ "	GROUP BY tf.title, tf.address, tf.category" )
	List<DidsDTO> findDibsByUserId(@Param("userId") String userId);
	
}
