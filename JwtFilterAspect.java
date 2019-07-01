package com.example.jwtdemo.jwt;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author defned
 */
@Aspect
@Component
public class JwtFilterAspect {

    @Autowired
    private JWTUtils  jwtUtils;

    @Pointcut("@annotation(com.example.jwtdemo.jwt.JwtFilter)")
    public void jwtFilterCut(){}

    @Before("jwtFilterCut()")
    public void jwtFilter(JoinPoint joinPoint) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Token");
        if (StringUtils.isEmpty(token)){
            throw new Exception("Token is empty");
        }
        Claims claims = jwtUtils.validateToken(token);

        if (null == claims){
            throw new Exception("Token validate error");
        }

        String s = claims.get("username").toString();
        System.out.println(s);

    }
}
