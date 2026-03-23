package com.schoolmap.mapper;

import com.schoolmap.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper{

    @Select("select * from map_user where username=#{username} and password=#{password}")
    User loginSelect(String username, String password);

    @Select("select * from map_user where username=#{username}")
    User queryByUsername(String username);

    List<User> queryById(int id);
    List<User> queryByIdList(List<Integer> ids);

    Boolean insertUser(User user);

    List<User> getUsersByPage(Long offset ,Long pageSize , Map<String, Object> conditions);

    Long countUserNums(Map<String, Object> conditions);

    Boolean updateUser(User user);

    Boolean deleteUser(List<User> userList);
}
