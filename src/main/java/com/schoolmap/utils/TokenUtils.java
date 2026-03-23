package com.schoolmap.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.schoolmap.constants.Constants;
import com.schoolmap.entity.User;
import com.schoolmap.exception.ServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
public class TokenUtils {

    public static String genToken(String userId, String username){
        String token = JWT.create()
                .withAudience(userId)
                .sign(Algorithm.HMAC256(username));
        return token;
    }


    public static User getCurrentUser(){
        try{
            return UserHolder.getUser();
        }catch (Exception e){
            return null;
        }
    }
    public static boolean validateLogin(){
        try{
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String token = request.getHeader("token");
            if(StringUtils.hasLength(token)){
                JWT.decode(token).getAudience();
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_401,"登录状态失效！");
        }
    }

    public static boolean validateAuthority(){
        try{
            User user = getCurrentUser();
            assert user != null;
            if(user.getPower().equals(0)){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        throw new ServiceException(Constants.CODE_403,"无权限！");
    }
}
