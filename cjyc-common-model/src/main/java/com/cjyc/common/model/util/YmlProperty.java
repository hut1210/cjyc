package com.cjyc.common.model.util;

import com.alibaba.fastjson.JSON;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
* 解析yml文件工具类
 * @author JPG
*/
public class YmlProperty {
/*    public static void main(String[] args) throws IOException {
        String path = "D:\\workspace_idea\\git\\branch\\cjyc-web-api\\src\\main\\resources\\";
        String[] list = new File(path).list();
        System.out.println(JSON.toJSONString(list));
        if(list == null || list.length == 0){
            return;
        }
        List<String> newList = new ArrayList<>();
        for (String fileName : list) {
            if(fileName.contains("application")){
                newList.add(fileName);
            }
        }
        if(newList.size() == 0){
            return;
        }
        String fileName = "";
        if(newList.size() == 1){
            fileName = newList.get(0);
        }else{
            InputStream inputStream = YmlProperty.class.getClassLoader().getResourceAsStream("bootstrap.yml");
            Properties prop = new Properties();
            prop.load(inputStream);
            String active = prop.getProperty("spring.profiles.active");
            for (String s : newList) {
                if(s.contains(active)){
                    fileName
                }
            }
        }

    }*/

    private static final String DEFAULT_FILE = "application.yml";
    private static Map<String, String> propertyMap = new HashMap<String, String>();

    /**
     * 获取value
     * @param key 属性键
     * @return
     */
    public static String get(String key) {
        return get(DEFAULT_FILE, key);
    }

    /**
     * 获取value
     * @author JPG
     * @date 2019/8/21 12:27
     * @param file ClassPath根文件名
     * @param key key 属性键
     */
    public static String get(String file, String key) {
        String value = null;
        if(file == null || DEFAULT_FILE.equals(file)){
            value = propertyMap.get(key);
        }
        if(value == null){
            putYmlPropertyInMap(file);
            value = propertyMap.get(key);
        }
        return value;
    }

    private static void putYmlPropertyInMap(String file) {
        if(file == null || "".equals(file.trim())){
            file = DEFAULT_FILE;
        }
        Yaml yaml = new Yaml();
        InputStream inputStream = YmlProperty.class.getClassLoader().getResourceAsStream(file);
        Iterator<Object> result = yaml.loadAll(inputStream).iterator();
        while (result.hasNext()) {
            Map map = (Map) result.next();
            iteratorMap(map, null);
        }
    }

    private static void iteratorMap(Map map, String key) {
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object lastKey = entry.getKey();
            String newKey = "";
            if (key != null && !"".equals(key.trim())) {
                newKey = key + "." + lastKey.toString();
            } else {
                newKey = lastKey.toString();
            }
            Object value = entry.getValue();
            if (value instanceof LinkedHashMap) {
                iteratorMap((Map) value, newKey);
            }
            if (value instanceof String || value instanceof Boolean || value instanceof Integer) {
                propertyMap.put(newKey, value.toString());
            }
        }
    }




}