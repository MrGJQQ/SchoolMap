package com.schoolmap.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.schoolmap.common.Result;
import com.schoolmap.constants.Constants;
import com.schoolmap.constants.RedisConstants;
import com.schoolmap.entity.LoginForm;
import com.schoolmap.entity.User;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.entity.dto.UserDTO;
import com.schoolmap.exception.ServiceException;
import com.schoolmap.mapper.UserMapper;
import com.schoolmap.service.UserService;
import com.schoolmap.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    RedisTemplate<String, User> redisTemplate;
    @Autowired
    UserMapper userMapper;


    //登录
    @Override
    public UserDTO login(LoginForm loginForm) {
        User user = userMapper.loginSelect(loginForm.getUsername(), loginForm.getPassword());

        if(user == null) {
            throw new ServiceException(Constants.CODE_403,"用户名或密码错误");
        }
        String token = TokenUtils.genToken(user.getId().toString(), user.getUsername());
        //把用户存到redis中
        redisTemplate.opsForValue().set(RedisConstants.USER_TOKEN_KEY + token,user);
        //jwt不设置过期时间，只设置redis过期时间。
        redisTemplate.expire(RedisConstants.USER_TOKEN_KEY +token, RedisConstants.USER_TOKEN_TTL, TimeUnit.MINUTES);
        //把查到的user的一些属性赋值给userDTO
        UserDTO userDTO = BeanUtil.copyProperties(user,UserDTO.class);
        //设置token
        userDTO.setToken(token);
        return userDTO;
    }

    //添加用户
    @Override
    public Boolean insertUser(User user) {
        User existingUser = userMapper.queryByUsername(user.getUsername());
        if (existingUser != null) {
            throw new ServiceException(Constants.CODE_403, "用户名已存在，无法添加");
        }
        return userMapper.insertUser(user);
    }


    //分页查询
    @Override
    public PageResultDTO<User> pageUsers(Long currentPage, Long pageSize, Map<String, Object> conditions) {

        Long offset = (currentPage - 1) * pageSize;
        // 获取分页数据
        List<User> userPage = userMapper.getUsersByPage(offset, pageSize, conditions);
        // 获取总记录数
        Long totalNums = userMapper.countUserNums(conditions);

        // 使用通用分页工具构建结果
        PageResultDTO<User> pageResult = new PageResultDTO<>(currentPage, pageSize, totalNums, userPage);

        return pageResult;
    }


    @Override
    public Boolean updateUser(User user) {
        if(user.getUsername() == null || user.getUsername().isEmpty()){
            throw new ServiceException(Constants.CODE_403, "用户名不能为空");
        }
        if(user.getPassword() == null){
            throw new ServiceException(Constants.CODE_403, "密码不能为空");
        }
        User existingUser = userMapper.queryByUsername(user.getUsername());
        if (existingUser == null) {
            throw new ServiceException(Constants.CODE_403, "用户不存在，无法修改");
        }
        return userMapper.updateUser(user);
    }

    //删除
    @Override
    public Boolean removeById(int id) {
        List<User> userList = userMapper.queryById(id);
        if (userList.isEmpty()){
            throw new ServiceException(Constants.CODE_403, "用户不存在");
        }
        return userMapper.deleteUser(userList);
    }

    //批量删除
    @Override
    public Boolean removeBatchByIds(List<Integer> ids) {
        List<User> userList = userMapper.queryByIdList(ids);
        if (userList.isEmpty()){
            throw new ServiceException(Constants.CODE_403, "用户不存在");
        }
        return userMapper.deleteUser(userList);
    }

    @Override
    public Result resetPassword(String id, String newPassword) {
        return null;
    }
}
