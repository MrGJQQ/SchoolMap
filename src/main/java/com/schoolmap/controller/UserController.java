package com.schoolmap.controller;

import com.schoolmap.annotation.Authority;
import com.schoolmap.common.Result;
import com.schoolmap.constants.Constants;
import com.schoolmap.entity.AuthorityType;
import com.schoolmap.entity.LoginForm;
import com.schoolmap.entity.User;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.entity.dto.UserDTO;
import com.schoolmap.service.UserService;
import com.schoolmap.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/*
这个注解表示该控制器下所有接口都可以通过跨域访问，注解内可以指定某一域名
也可以配置config类
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm) {
        UserDTO dto = userService.login(loginForm);
        return Result.success(dto);
    }

    @PostMapping("/adduser")
    public Result insertUser(@RequestBody User user) {
        Boolean resultUser = userService.insertUser(user);
        return Result.success(resultUser);
    }


    @GetMapping("/userid")
    public long getUserId() {
        return TokenUtils.getCurrentUser().getId();
    }

    @GetMapping
    public Result pageUsers(@RequestParam(defaultValue = "1") Long currentPage,
                            @RequestParam(defaultValue = "10") Long pageSize,
                            @RequestParam(required = false) String username,
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) String phone,
                            @RequestParam(required = false) Integer power) {
        Map<String, Object> params = new HashMap<>();
        if (username != null && !username.isEmpty()) {
            params.put("username", username);
        }
        if (name != null && !name.isEmpty()) {
            params.put("name", name);
        }
        if (phone != null && !phone.isEmpty()) {
            params.put("phone", phone);
        }
        if (power != null) {
            params.put("power", power);
        }
        PageResultDTO<User> pageResult = userService.pageUsers(currentPage, pageSize, params);
        return Result.success(pageResult);
    }

    //修改用户
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {

        return Result.success(userService.updateUser(user));
    }

    @Authority(AuthorityType.requireAuthority)
    @DeleteMapping("/userDelete/{id}")
    public Result deleteById(@PathVariable int id) {
        Boolean isSuccessful = userService.removeById(id);
        if (isSuccessful) {
            return Result.success("删除成功");
        } else {
            return Result.error(Constants.CODE_500, "删除失败");
        }
    }

    @Authority(AuthorityType.requireAuthority)
    @PostMapping("/userDeleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        Boolean isSuccessful = userService.removeBatchByIds(ids);
        if (isSuccessful) {
            return Result.success("删除成功");
        } else {
            return Result.error(Constants.CODE_500, "删除失败");
        }
    }


    /**
     * 重置密码
     *
     * @param id          用户id
     * @param newPassword 新密码
     * @return 结果
     */
    @GetMapping("/user/resetPassword")
    public Result resetPassword(@RequestParam String id, @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return Result.success();
    }
}
