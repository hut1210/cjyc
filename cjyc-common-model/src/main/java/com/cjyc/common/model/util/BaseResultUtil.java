package com.cjyc.common.model.util;


import com.cjyc.common.model.dto.ListVo;
import com.cjyc.common.model.dto.ResultVo;

import java.util.List;
import java.util.Map;

/**
 * 封装返回数据工具
 * @author JPG
 */
public abstract class BaseResultUtil<T> {

    /**
     * 获取ResultVo<T>(无内容)
     * @author JPG
     * @date 2019/7/31 9:47
     * @param code 返回码
     * @param message 返回信息
     * @return ResultVo
     */
    public static ResultVo getVo(int code, String message){
        return getVo(code, message, null);
    }

    /**
     * 获取ResultVo<T>
     * @author JPG
     * @date 2019/7/31 10:02
     * @param code 返回码
     * @param message 返回信息
     * @param data 返回内容
     * @return ResultVo
     */
    public static <T> ResultVo<T> getVo(int code, String message, T data){
        return ResultVo.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 获取ResultVo<ListVo<T>>
     * @description 结果无分页，不带分页相关统计信息，带非分页相关信息集合countInfo
     * @author JPG
     * @date 2019/7/31 10:46
     * @param code 返回码
     * @param message 返回信息
     * @param list 返回内容
     * @param countInfo 非分页相关统计信息
     * @return  ResultVo<ListVo<T>>
     */
    public static <T> ResultVo<ListVo<T>> getListVo(int code, String message, List<T> list, Map<String, Object> countInfo){
        Long totalRecords = null;
        if(countInfo != null){
            try {
                totalRecords = countInfo.get("totalCount") == null ? null : (long)countInfo.get("totalCount");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        ListVo<T> listVo = ListVo.<T>builder()
                .totalRecords(totalRecords)
                .list(list)
                .countInfo(countInfo)
                .build();
        return ResultVo.<ListVo<T>>builder()
                .code(code)
                .message(message)
                .data(listVo)
                .build();

    }

}
