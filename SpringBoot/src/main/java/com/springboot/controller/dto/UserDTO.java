package com.springboot.controller.dto;

import com.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String icon;
    private String token;
    private String role;
    private List<Menu> menus;
}
