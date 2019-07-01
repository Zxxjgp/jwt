package com.example.jwtdemo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author jiaoguanping
 * @version 1.0.0
 * @ClassName JWTUtils
 * @date 2019/7/1  16:12
 */
@Component
public class JWTUtils {

    /**
     * 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了
     */
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private final long validityInMilliseconds = 60000;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    public String createToken() {

        /**
         * 存放的数据
         */
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", "123");
        claims.put("username", "jgg");
        claims.put("password", "123456");

        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date date = new Date(nowMillis + validityInMilliseconds);

        return Jwts.builder().setClaims(claims).setIssuedAt(new Date())
                .setExpiration(date).signWith(SignatureAlgorithm.HS256, secretKey)
                .setSubject("jgp")
                .compact();

    }

    public Claims validateToken(String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return body;
        } catch (Exception e) {
        }
        return null;
    }
}