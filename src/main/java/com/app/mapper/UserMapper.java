package com.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.app.dto.ReviewDTO;
import com.app.dto.RoleDTO;
import com.app.dto.UserDTO;  

@Mapper
public interface UserMapper {

	@Select("SELECT userId, password "
			+ "FROM Users "
			+ "WHERE userId = #{userId} "
			+ "AND password = #{password} ")
	public UserDTO findByUser(UserDTO userDTO);
	
	@Select(" SELECT r.roleNm "
			+ "	 FROM Users AS ur "
			+ "		INNER JOIN role AS r "
			+ "		ON ur.status = r.roleNo "
			+ "		 WHERE ur.userNo = #{userNo} ")
	public RoleDTO findByRoles(int userNo);
	 
	@Insert("INSERT INTO `Users` (" 
	+"  `userId`, `password`, `name`, `user_Nnm`, `phone`, `email`, `createDate`, `modifiedDate`, "
	+" `status`, `review`, `profileImageUrl`" 
	+" ) VALUES (" 
	+"   #{userId}, #{password}, #{name}, #{user_Nnm}, #{phone}, #{email}, NOW(), NOW(), " 
	+ " 1, 'This is a sample review.', 'user1.png'" 
	+");")
	int signup(UserDTO dto);
	
	@Select("SELECT COUNT(*) FROM Users WHERE userId = #{userId}")
	int checkUserIdDuplicate(String userId);
	
	@Select("SELECT `userNo`,`userId`, `password`, `user_Nnm`, `phone`,`status`, `email`,`review`, `profileImageUrl` FROM Users WHERE userId = #{userId}")
	public UserDTO findOne(String userId);
	
	@Update("UPDATE Users SET user_Nnm = #{user_Nnm}, phone = #{phone}, email = #{email}, "
		  + "password = #{password}, review= #{review}, profileImageUrl = #{profileImageUrl} WHERE userId = #{userId}")
    void updateUser(UserDTO userDTO);
	
	@Select("  SELECT r.reviewNo, r.storeId, ur.userId, r.menuId, r.rating, r.content ,r.createDate"
			+ "	FROM Users AS ur "
			+ "	INNER JOIN Reviews AS r "
			+ "	ON ur.userId = #{userId} ")
	List<ReviewDTO> findReviewsByStatus(@Param("userId") String userId);

	@Update("UPDATE Reviews " +
			"SET content = #{content}, rating = #{rating}, createDate = NOW() " +
			"WHERE reviewNo = #{reviewNo}")
	int updateReview(ReviewDTO reviewDTO);

}

