package com.ecommerce.dto;


import com.ecommerce.enums.UserRole;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDto {

    private Long id;

    private String email;

    private String name;

    private UserRole role;

    private MultipartFile img;

    private byte[] returnedImg;

}
