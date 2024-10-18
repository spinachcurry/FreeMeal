package com.app.userDTO;
// LoginResponseDTO.java
public class LoginResponseDTO {
    private boolean status;
    private UserDTO userDTO;
    private String jwtToken;

    public LoginResponseDTO(boolean status, UserDTO userDTO, String jwtToken) {
        this.status = status;
        this.userDTO = userDTO;
        this.jwtToken = jwtToken;
    }

    // Getters and Setters
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
