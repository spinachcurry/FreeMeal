package com.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.app.dto.DataDTO;
import com.app.userDTO.DidsDTO;
import com.app.userDTO.ReviewDTO;
import com.app.userDTO.RoleDTO;
import com.app.userDTO.UserDTO;  

@Mapper
public interface UserMapper {

	@Select("SELECT userId, password "
			+ "FROM Users "
			+ "WHERE userId = #{userId} "
			+ "AND password = #{password} ")
	public UserDTO findByUser(UserDTO userDTO);
	
	@Select(" SELECT r.roleNm "
			+ "	 FROM Users AS ur "
			+ "	 INNER JOIN role AS r "
			+ "	 ON ur.status = r.roleNo "
			+ "	 WHERE ur.status = #{userNo} ")
	public List<RoleDTO> findByRoles(String status);
	 
	@Insert("INSERT INTO `Users` (" 
			+"  `userId`, `password`, `name`, `user_Nnm`, `phone`, `email`, `createDate`, `modifiedDate`, "
			+"  `status`, `review`, `profileImageUrl`" 
			+"  ) VALUES (" 
			+"  #{userId}, #{password}, #{name}, #{user_Nnm}, #{phone}, #{email}, NOW(), NOW(), " 
			+"  #{status}, #{review}, #{profileImageUrl} ); ")
	int signup(UserDTO dto);
	
	@Select("SELECT COUNT(*) FROM Users WHERE userId = #{userId}")
	int checkUserIdDuplicate(String userId);
	
	@Select("SELECT `userNo`,`userId`, `password`, `user_Nnm`, `phone`,`status`, `email`,`review`, `profileImageUrl` FROM Users WHERE userId = #{userId}")
	public List<UserDTO> findOne(String userId);
	
	@Update("UPDATE Users SET user_Nnm = #{user_Nnm}, phone = #{phone}, email = #{email}, "
		  + "password = #{password}, review= #{review}, profileImageUrl = #{profileImageUrl} WHERE userId = #{userId}")
    void updateUser(UserDTO userDTO);
	
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
	
	@Update("UPDATE Reviews " 
			+ "SET address = #{address}, userId=#{userId}, content = #{content}, modifiedDate = NOW(), status='일반' " 
			+ "WHERE reviewNo = #{reviewNo} ")
	int updateReview(ReviewDTO reviewDTO);
	
	//////가게 상세 페이지리뷰 불러오기(리뷰용)
	@Select("	SELECT  tf.title, tf.address, tf.category, SUM(tf.price) AS totalPrice, SUM(tf.party) AS totalParty, "
			+ "			re.reviewNo, re.userId, re.content, re.modifiedDate,re.createDate, re.status "
			+ "			FROM freemeal AS tf "
			+ "			INNER JOIN Reviews AS re "
			+ "			ON tf.address = re.address "
			+ "			WHERE tf.address = #{address} AND STATUS ='일반'"
			+ "			GROUP BY tf.title, tf.address, tf.category, re.userId, re.content, re.modifiedDate, re.status"
			+ " 		ORDER BY re.modifiedDate DESC; ")
	List<ReviewDTO> FindStoreOne(@Param("address") String address);
	   
	@Insert("INSERT INTO Reviews (address, userId, content, createDate, modifiedDate) " +
	        "VALUES (#{address}, #{userId}, #{content}, NOW(), NOW())")
	int insertReview(ReviewDTO reviews); 
	
	@Update("UPDATE Reviews SET STATUS = '신고' WHERE reviewNo =#{reviewNo} ")
	int updateReport(ReviewDTO reviewNo);
	//찜하기 
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
			+ "			 FROM freemeal AS tf "
			+ "		 	 INNER JOIN Dibs AS did "
			+ "			 ON tf.address = did.address "
			+ "			 WHERE did.STATUS = '1' AND did.userId =#{userId} "
			+ "		 	GROUP BY tf.title, tf.address, tf.category" )
	List<DidsDTO> findDibsByUserId(@Param("userId") String userId);
	
}

