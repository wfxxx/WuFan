package com.definesys.dsgc.service.utils.httpclient;

import java.io.Serializable;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/7/3 上午10:09
 * @history: 1.2019/7/3 created by biao.luo
 */
public class ResultVO<T>  implements Serializable{
    public ResultVO() {
    }

//    private Integer code;
//
//    private String message;

    private T data;

//    public Integer getCode() {
//        return code;
//    }
//
//    public void setCode(Integer code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
