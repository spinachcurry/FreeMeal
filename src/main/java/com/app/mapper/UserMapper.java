package com.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
	
	@Insert ("INSERT INTO `Users` ("
			+ "  `userId`, `password`, `name`, `user_Nnm`, `phone`, `email`, `createDate`, `modifiedDate`,  `review`"
			+ ") VALUES ("
			+ "   #{userId}, #{password}, #{name}, #{user_Nnm}, #{phone}, #{email}, NOW(), NOW(),  'This is a sample review.' "
			+ ");")
	public UserDTO signup(UserDTO dto);
	
}
