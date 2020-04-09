package com.definesys.dsgc.service.utils;

import org.springframework.stereotype.Component;

/**
 * @ClassName: InitializerUtil
 * TODO:
 * @Author: xzy
 * @UpdateUser: xzy
 * @Version: 0.0.1
 */
@Component
public class InitializerUtil {
    private TokenSettings tokenSettings;
    public InitializerUtil(TokenSettings tokenSettings){
        JwtTokenUtil.setTokenSettings(tokenSettings);
    }
}
