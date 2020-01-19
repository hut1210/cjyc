package com.cjyc.common.model.util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PositionUtil {

    /**
     * Baidu地图通过地址获取经纬度
     */
    public static String getLngAndLat(String address) {
        String location = "";
        address = address.replace(" ", "");
        //http://api.map.baidu.com/geocoder/v2/?address="地址"&output=json&ak="giPSqLWGrcvOuh9HFPafnT6AoUGaXOjQ"
        String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=nSxiPohfziUaCuONe4ViUP2N";
        try {
            String json = loadJSON(url);
            JSONObject obj = JSONObject.parseObject(json);
            if (obj.get("status").toString().equals("0")) {
                double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
                double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
                location = lng + "," + lat;
            } else {
                System.out.println("未找到相匹配的经纬度！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        }
        return json.toString();
    }

    public static void main(String[] args) {
        String location = getLngAndLat("徐州市");
        System.out.println(location);
        String location2 = getLngAndLat("北京市");
        System.out.println(location2);
        double distance = getDistance(Double.valueOf(location.split(",")[0]), Double.valueOf(location.split(",")[1]), Double.valueOf(location2.split(",")[0]), Double.valueOf(location2.split(",")[1]));
        System.out.println(new BigDecimal(distance));
        BigDecimal bd = new BigDecimal(distance).setScale(0, BigDecimal.ROUND_DOWN);
        System.out.println(bd);
    }

    /**
     * 补充：计算两点之间真实距离
     *
     * @return km
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 维度
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;
        // 经度
        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;
        // 地球半径
        double R = 6371;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        return Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
    }
}
