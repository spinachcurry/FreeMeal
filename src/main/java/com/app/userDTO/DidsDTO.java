package com.app.userDTO;
 
import java.util.List;  
import com.app.dto.StoreDTO; 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DidsDTO {
	 
	private String userId;
	private String address;
	private int status; 
	private int totalPrice;
	private String title;
	private String category;
	 

}
