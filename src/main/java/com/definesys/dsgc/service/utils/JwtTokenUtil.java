package com.definesys.dsgc.service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtTokenUtil {

    /**
     * 签发token
     * @Author:      xzy
     * @UpdateUser:
     * @Version:     0.0.1
     * @param issuer 签发人
     * @param expScond 有效时间(秒)
     * @return       java.lang.String
     * @throws
     */
    public static String generateToken(String issuer,long expScond, String secret) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        String completeSecretKey = secret;
        byte[] signingKey = DatatypeConverter.parseBase64Binary(completeSecretKey);
        JwtBuilder builder = Jwts.builder();
        Map headerMap = new HashMap();

        Map<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        builder.setHeader(header);
        if (!StringUtils.isEmpty(issuer)) {
            builder.setIssuer(issuer);
        }
        if (expScond >= 0) {
            Date exp = new Date(expScond*1000);
            builder.setExpiration(exp);
        }
        builder.signWith(signatureAlgorithm, completeSecretKey.getBytes());
        return builder.compact();
    }

    /**
     * 从令牌中获取数据声明
     * @Author:      xzy
     * @UpdateUser:
     * @Version:     0.0.1
     * @param token
     * @return       io.jsonwebtoken.Claims
     * @throws
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("test")).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }



    /**
     * 验证token 是否过期
     * @Author:      xzy
     * @UpdateUser:
     * @Version:     0.0.1
     * @param token
     * @param secretKey
     * @return       java.lang.Boolean
     * @throws
     */
    public static Boolean isTokenExpired(String token) {

        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {

            return true;
        }
    }
    /**
     * 校验令牌
     * @Author:      xzy
     * @UpdateUser:
     * @Version:     0.0.1
     * @param token
     * @return       java.lang.Boolean
     * @throws
     */
    public static Boolean validateToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return (null!=claimsFromToken && !isTokenExpired(token));
    }


    /**
     * 获取token的剩余过期时间
     * @Author:      xzy
     * @UpdateUser:
     * @Version:     0.0.1
     * @param token
     * @param secretKey
     * @return       long
     * @throws
     */
    public static long getRemainingTime(String token){
        long result=0;
        try {
            long nowMillis = System.currentTimeMillis();
            result= getClaimsFromToken(token).getExpiration().getTime()-nowMillis;
        } catch (Exception e) {

        }
        return result;
    }
}
