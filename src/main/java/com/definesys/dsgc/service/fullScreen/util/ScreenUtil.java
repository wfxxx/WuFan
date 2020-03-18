package com.definesys.dsgc.service.fullScreen.util;

import net.sf.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenUtil {


    /**
     * 获取边界值：最大值和最小值
     *
     * @param dataList 数据集合
     * @param key      关键字
     * @param minRate  最小值比重，区间值最小坐标，一般为0
     * @param maxRate  最大值比重，区间值最大坐标，
     * @return
     */
    public static JSONObject getBorderNumberByData(List<Map<String, Object>> dataList, String key, double minRate, double maxRate) {
        JSONObject jsonObject = new JSONObject();
        long minValue = 0;
        long maxValue = 0;
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> dataMap = dataList.get(i);
                Object value = dataMap.get(key);
                if (value != null && !"".equals(String.valueOf(value))) {
                    int num = Integer.parseInt(String.valueOf(value));
                    //System.out.println("num->" + num);
                    minValue = Math.min(num, minValue);
                    maxValue = Math.max(num, maxValue);
                }
            }


        }
        jsonObject.put("min", minValue);
        jsonObject.put("max", maxValue);
        //System.out.println("min->" + minValue + " max->" + maxValue);
        return jsonObject;
    }

    /**
     * @param value
     * @param type
     * @param rate
     * @return
     */
    public static double getBorderNumber(double value, String type, double rate) {
        double rtnValue = 0;
        if (value > 0 && value > 0) {
            if ("min".equals(type)) {
                //最小值坐标
                double min = value - value * rate;
                if (min > 0) {
                    rtnValue = Math.round(min);
                }
            } else {
                double max = value + value * rate;
                rtnValue = Math.round(max);

            }
        }
        return rtnValue;
    }


    public static void main(String[] args) {
//        List<Map<String, Object>> list = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("value", "0");
//        list.add(map);
//        map = new HashMap<>();
//        map.put("value", "0");
//        list.add(map);
//        map = new HashMap<>();
//        map.put("value", "0");
//        list.add(map);
//        map = new HashMap<>();
//        map.put("value", "0");
//        list.add(map);
//        map = new HashMap<>();
//        map.put("value", "1");
//        list.add(map);
//        getBorderNumberByData(list, "value", 0, 0.1);


        getBorderNumber(2, "min", 0.1);
    }

}
