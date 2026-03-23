package com.schoolmap.entity.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String username;
    private String name;
    private String photo;
    private String token;
    private String power;

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + name + '\'' +
                ", avatarUrl='" + photo + '\'' +
                ", token='" + token + '\'' +
                ", role='" + power + '\'' +
                '}';
    }
}
