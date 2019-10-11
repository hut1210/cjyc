package com.cjyc.salesman.api.service;

import com.alibaba.fastjson.JSON;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dao.IZdataCityDao;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.entity.ZdataCity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ZcityService {

    @Resource
    private IZdataCityDao zdataCityDao;

    @Resource
    private ICityDao cityDao;
    public void changeArea(){
        List<ZdataCity> list = zdataCityDao.findList();
        for (ZdataCity zdataCity : list) {


        }

    }




/*

    public int pickParentCode(){

        Set<String> set5 = new HashSet<>();
        Set<String> set4 = new HashSet<>();
        Set<String> set3 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        Set<String> set1 = new HashSet<>();
        Set<String> set0 = new HashSet<>();

        int c5 = 0;
        int c4 = 0;
        int c3 = 0;
        int c2 = 0;
        int c1 = 0;
        int c0 = 0;
        List<ZdataCity> list = zdataCityDao.findList();
        for (ZdataCity zdataCity : list) {
            String code = zdataCity.getCode();

            if(StringUtils.isBlank(code)){
                continue;
            }
            if(code.endsWith("00000")){
                set5.add(code);
                c5++;
                continue;
            }
            if(code.endsWith("0000")){
                set4.add(code);
                c4++;
                continue;
            }

            if(code.endsWith("000")){
                set3.add(code);
                c3++;
                zdataCity.setGrandpaCode(code.substring(0, 2) + "0000");
                zdataCity.setGrandpaName("2");
                zdataCityDao.updateById(zdataCity);
                continue;
            }
            if(code.endsWith("00")){
                set2.add(code);
                c2++;
                zdataCity.setGrandpaCode(code.substring(0, 2) + "0000");
                zdataCity.setGrandpaName("2");
                zdataCityDao.updateById(zdataCity);
                continue;
            }
            if(code.endsWith("0")){
                set1.add(code);
                c1++;
                zdataCity.setGrandpaCode(code.substring(0, 4) + "00");
                zdataCity.setGrandpaName("3");
                zdataCityDao.updateById(zdataCity);
                continue;
            }

            set0.add(code);
            c0++;
            zdataCity.setGrandpaCode(code.substring(0, 4) + "00");
            zdataCity.setGrandpaName("3");
            zdataCityDao.updateById(zdataCity);

        }
        System.out.println();
        System.out.println("五个零：" + c5 + "个" + JSON.toJSONString(set5));
        System.out.println("四个零：" + c4 + "个" + JSON.toJSONString(set4));
        System.out.println("三个零：" + c3 + "个" + JSON.toJSONString(set3));
        System.out.println("二个零：" + c2 + "个" + JSON.toJSONString(set2));
        System.out.println("一个零：" + c1 + "个" + JSON.toJSONString(set1));
        System.out.println("零个零：" + c0 + "个" + JSON.toJSONString(set0));

        System.out.println(c5 + c4 + c3 + c2 + c1 + c0);
        return c5 + c4 + c3 + c2 + c1+ c0;

    }


*/















}
