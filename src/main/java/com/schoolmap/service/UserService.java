package com.schoolmap.service;

import com.schoolmap.common.Result;
import com.schoolmap.entity.LoginForm;
import com.schoolmap.entity.User;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.entity.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDTO login(LoginForm loginForm);

    Boolean insertUser(User user);

    PageResultDTO<User> pageUsers(Long currentPage, Long pageSize, Map<String, Object> params);

    Boolean updateUser(User user);

    Boolean removeById(int id);

    Boolean removeBatchByIds(List<Integer> ids);

    Result resetPassword(String id, String newPassword);
}
