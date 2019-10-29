package com.cjyc.common.model.util;

import com.cjyc.common.model.vo.web.city.CityTreeVo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


//树形结构工具类
public class TreeUtils {

	    public static List<CityTreeVo> cityCommon;
	    public static List<Object> list = new ArrayList<>();
	 
	    public static List<Object> treeCity(List<CityTreeVo> cityTreeVoList){
			cityCommon = cityTreeVoList;
	        for (CityTreeVo cityTreeVo : cityTreeVoList) {
	            Map<String,Object> mapArr = new LinkedHashMap<>();
	            //我的数据库 `TYPE` int(4) DEFAULT NULL COMMENT '部门类型: 1根部门(企业),2二级单位,3三级部门,依次类推
	            //这个根据需求,有可能父部门id是一样的,那就在判断语句中把父部门的id等于多少作为if中的条件
	            if (-1 == cityTreeVo.getLevel()) {
	            	setTreeMap(mapArr,cityTreeVo);
	                list.add(mapArr);
				}
	          
	        }
	        return list;
	    }
	    
	    //这里的fid参数是父id
	    public static List<Map<String,Object>> menuChild(String parentId){
	        List<Map<String,Object>> lists = new ArrayList<>();
	        for(CityTreeVo vo:cityCommon){
	            Map<String,Object> childArray = new LinkedHashMap<>();
	            if (vo.getCode().equals(parentId)) {
	            	   setTreeMap(childArray,vo);
		               lists.add(childArray);
				}
	           
	        }
	        return lists;
	    }
	    
	    //类结构
	    private static void setTreeMap(Map<String,Object> mapArr,CityTreeVo cityTreeVo){
	    	mapArr.put("parentCode", cityTreeVo.getParentCode());
	    	mapArr.put("code", cityTreeVo.getCode());
	        mapArr.put("name", cityTreeVo.getName());
			mapArr.put("level", cityTreeVo.getLevel());
	        mapArr.put("children", menuChild(cityTreeVo.getParentCode()));
	    }
	
}


