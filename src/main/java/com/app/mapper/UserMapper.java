package com.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
			+ "		INNER JOIN role AS r "
			+ "		ON ur.status = r.roleNo "
			+ "		 WHERE ur.status = #{userNo} ")
	public List<RoleDTO> findByRoles(String status);
	
	@Insert("INSERT INTO `Users` (" 
	+"  `userId`, `password`, `name`, `user_Nnm`, `phone`, `email`, `createDate`, `modifiedDate`, "
	+" `status`, `review`, `profileImageUrl`" 
	+" ) VALUES (" 
	+"   #{userId}, #{password}, #{name}, #{user_Nnm}, #{phone}, #{email}, NOW(), NOW(), " 
	+ " #{status}, #{review}, #{profileImageUrl}" 
	+");")
	int signup(UserDTO dto);
	
	@Select("SELECT COUNT(*) FROM Users WHERE userId = #{userId}")
	int checkUserIdDuplicate(String userId);
	
	@Select("SELECT `userNo`,`userId`, `password`, `user_Nnm`, `phone`, `email`, `profileImageUrl` FROM Users WHERE userId = #{userId}")
	public List<UserDTO> findOne(String userId);
	
	@Update("UPDATE Users SET user_Nnm = #{user_Nnm}, phone = #{phone}, email = #{email}, password = #{password}, profileImageUrl = #{profileImageUrl} WHERE userId = #{userId}")
    void updateUser(UserDTO userDTO);
	
}
