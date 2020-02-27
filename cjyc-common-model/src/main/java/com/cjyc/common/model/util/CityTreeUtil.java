package com.cjyc.common.model.util;

import com.cjyc.common.model.vo.CityTreeVo;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

public class CityTreeUtil {

    /**
     * 封装树形结构 List集合转成树形结构
     * @param cityTreeVos
     * @return
     */
    public static List<CityTreeVo> encapTree(List<CityTreeVo> cityTreeVos){
        List<CityTreeVo> nodeList = new ArrayList<>();
        for(CityTreeVo nodeOne : cityTreeVos){
            boolean mark = false;
            for(CityTreeVo nodeTwo:cityTreeVos){
                if(nodeOne.getParentCode().equals(nodeTwo.getCode())){
                    mark = true;
                    if(nodeTwo.getCityVos() == null){
                        nodeTwo.setCityVos(new ArrayList<>());
                    }
                    nodeTwo.getCityVos().add(nodeOne);
                    break;
                }
            }
            if(!mark){
                nodeList.add(nodeOne);
            }
        }
        return nodeList;
    }


}