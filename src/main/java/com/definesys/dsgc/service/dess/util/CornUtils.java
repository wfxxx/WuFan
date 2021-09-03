package com.definesys.dsgc.service.dess.util;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.dess.DessInstance.bean.DInstBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import com.definesys.dsgc.service.utils.httpclient.ResultVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CornUtils
 * @Description corn表达式转换工具
 * @Author ystar
 * @Date 2020-7-28 18:24
 * @Version 1.0
 **/
public class CornUtils {

    /**
     * 调用DESS接口，根据表达式获取最近10次运行时刻
     *
     * @param dessServiceUrl
     * @param cornExpression
     * @param aliveStart
     * @param aliveEnd
     * @return
     */
    public static List<Map<String, String>> getNextTenRunTimes(HttpServletRequest request, String dessServiceUrl, String cornExpression, Date aliveStart, Date aliveEnd) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jobFrequency", StringUtil.isBlank(cornExpression) ? "" : cornExpression);
        jsonObject.put("aliveStart", aliveStart == null ? new Date() : aliveStart);
        jsonObject.put("aliveEnd", aliveEnd == null ? new Date() : aliveEnd);
        System.out.println(jsonObject);
        ResultVO<List<Map<String, String>>> result = HttpReqUtil.sendPostRequest(dessServiceUrl + "/dess/getNextTenRunTimes", jsonObject, request);
        return result.getData();
    }
}
