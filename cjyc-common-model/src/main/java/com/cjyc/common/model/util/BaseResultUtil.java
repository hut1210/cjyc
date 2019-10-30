package com.cjyc.common.model.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * 封装返回数据工具
 * @author JPG
 */
public class BaseResultUtil<T> {




    /**
     * 快速返回成功
     * @author JPG
     * @since 2019/10/9 11:49
     * @param
     * @return
     */
    public static <T> ResultVo<T> success(){
        return getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
    }

    public static <T> ResultVo<T> success(T data){
        return getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }

    public static <T> ResultVo<ListVo<T>> success(List<T> list,  Map<String, Object> countInfo){
        return getListVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), list, null, countInfo);
    }
    public static <T> ResultVo<ListVo<T>> success(List<T> list, Long totalRecords){
        return getListVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), list, totalRecords, null);
    }
    public static <T> ResultVo<ListVo<T>> success(List<T> list, Long totalRecords, Map<String, Object> countInfo){
        return getListVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), list, totalRecords, countInfo);
    }
    public static <T> ResultVo<PageVo<T>> success(PageInfo<T> pageInfo){
        return getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), pageInfo);
    }

    public static <T> ResultVo<PageVo<T>> success(PageInfo<T> pageInfo, Map<String, Object> countInfo){
        return getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), pageInfo, countInfo);
    }

    public static <T> ResultVo<PageVo<T>> success(IPage<T> page){
        return getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), page);
    }

    public static <T> ResultVo<PageVo<T>> success(IPage<T> page, Map<String, Object> countInfo){
        return getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), page, countInfo);
    }

    /**
     * 快速返回失败
     * @author JPG
     * @since 2019/10/9 11:49
     * @param
     * @return
     */
    public static <T> ResultVo<T> fail(){
        return fail(null);
    }
    public static <T> ResultVo<T> fail(String message){
        return getVo(ResultEnum.FAIL.getCode(), message);
    }

    public static <T> ResultVo<T> fail(String message, String... args){
        return getVo(ResultEnum.FAIL.getCode(), MessageFormat.format(message, args));
    }

    /**
     * 快速返回参数错误
     * @author JPG
     * @since 2019/10/9 11:49
     * @param
     * @return
     */
    public static <T> ResultVo<T> paramError(){
        return paramError(ResultEnum.MOBILE_PARAM_ERROR.getMsg());
    }
    public static <T> ResultVo<T> paramError(String msg){
        return getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(), msg);
    }

    /**
     * 快速返回服务器错误
     * @author JPG
     * @since 2019/10/9 11:49
     * @param
     * @return
     */
    public static <T> ResultVo<T> serverError(){
        return serverError(ResultEnum.API_INVOKE_ERROR.getMsg());
    }
    public static <T> ResultVo<T> serverError(String msg){
        return getVo(ResultEnum.API_INVOKE_ERROR.getCode(), msg);
    }

    /**
     * 获取ResultVo<T>(无内容)
     * @author JPG
     * @date 2019/7/31 9:47
     * @param code 返回码
     * @param msg 返回信息
     * @return ResultVo
     */
    public static<T> ResultVo<T> getVo(int code, String msg){
        return getVo(code, msg, null);
    }

    /**
     * 获取ResultVo<T>
     * @author JPG
     * @date 2019/7/31 10:02
     * @param code 返回码
     * @param msg 返回信息
     * @param data 返回内容
     * @return ResultVo
     */
    public static <T> ResultVo<T> getVo(int code, String msg, T data){
        return ResultVo.<T>builder()
                .code(code)
                .msg(msg)
                .data(data)
                .build();
    }

    /**
     * 获取ResultVo<ListVo<T>>
     * @description 结果无分页，不带分页相关统计信息，带非分页相关信息集合countInfo
     * @author JPG
     * @date 2019/7/31 10:46
     * @param code 返回码
     * @param msg 返回信息
     * @param list 返回内容
     * @param countInfo 非分页相关统计信息
     * @return  ResultVo<ListVo<T>>
     */
    public static <T> ResultVo<ListVo<T>> getListVo(int code, String msg, List<T> list, Long totalRecords, Map<String, Object> countInfo){
        if(totalRecords == null){
            if(countInfo != null){
                try {
                    Object totalCount = countInfo.get("totalCount");
                    if(totalCount == null || StringUtils.isBlank(totalCount.toString())){
                        totalRecords = 0L;
                    }else{
                        totalRecords = Long.valueOf(totalCount.toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        ListVo<T> listVo = ListVo.<T>builder()
                .totalRecords(totalRecords)
                .list(list)
                .countInfo(countInfo)
                .build();
        return ResultVo.<ListVo<T>>builder()
                .code(code)
                .msg(msg)
                .data(listVo)
                .build();

    }

    /**
     * 获取ResultVo<PageVo<T>>
     * @description 结果带分页(PageHelper)，带分页相关统计信息
     * @author JPG
     * @date 2019/7/31 10:23
     * @param code 返回码
     * @param msg 返回信息
     * @param pageInfo 返回内容
     * @return ResultVo<PageVo<T>>
     */
    public static <T> ResultVo<PageVo<T>> getPageVo(int code, String msg, PageInfo<T> pageInfo){
        PageVo<T> pageVo = PageVo.<T>builder()
                .totalRecords(pageInfo.getTotal())
                .totalPages(pageInfo.getPages())
                .currentPage(pageInfo.getPageNum())
                .pageSize(pageInfo.getSize())
                .list(pageInfo.getList())
                .build();

        return ResultVo.<PageVo<T>>builder()
                .code(code)
                .msg(msg)
                .data(pageVo)
                .build();

    }

    /**
     * 获取ResultVo<PageVo<T>>
     * @description 结果带分页(PageHelper)，带分页相关统计信息，带非分页相关信息集合countInfo
     * @author JPG
     * @date 2019/7/31 10:25
     * @param code 返回码
     * @param msg 返回信息
     * @param pageInfo 返回内容
     * @param countInfo 非分页相关统计信息
     * @return ResultVo<PageVo<T>>
     */
    public static <T> ResultVo<PageVo<T>> getPageVo(int code, String msg, PageInfo<T> pageInfo, Map<String, Object> countInfo){
        PageVo<T> pageVo = PageVo.<T>builder()
                .totalRecords(pageInfo.getTotal())
                .totalPages(pageInfo.getPages())
                .currentPage(pageInfo.getPageNum())
                .pageSize(pageInfo.getSize())
                .list(pageInfo.getList())
                .countInfo(countInfo)
                .build();
        return ResultVo.<PageVo<T>>builder()
                .code(code)
                .msg(msg)
                .data(pageVo)
                .build();

    }

    /**
     * 获取ResultVo<PageVo<T>>
     * @author JPG
     * @date 2019/7/31 14:09
     * @param code 返回码
     * @param msg 返回信息
     * @param page 返回内容
     * @return ResultVo<PageVo<T>>
     */
    public static <T> ResultVo<PageVo<T>> getPageVo(int code, String msg, IPage<T> page){
        PageVo<T> pageVo = PageVo.<T>builder()
                .totalRecords(page.getTotal())
                .totalPages((int)page.getPages())
                .currentPage((int)page.getCurrent())
                .pageSize((int)page.getSize())
                .list(page.getRecords())
                .build();
        return ResultVo.<PageVo<T>>builder()
                .code(code)
                .msg(msg)
                .data(pageVo)
                .build();

    }
    /**
     * 获取ResultVo<PageVo<T>>
     * @description 结果带分页(Mybatis-plus)，带分页相关统计信息，带非分页相关信息集合countInfo
     * @author JPG
     * @date 2019/7/31 14:09
     * @param code 返回码
     * @param msg 返回信息
     * @param page 返回内容
     * @param countInfo 非分页相关统计信息
     * @return ResultVo<PageVo<T>>
     */
    public static <T> ResultVo<PageVo<T>> getPageVo(int code, String msg, IPage<T> page, Map<String, Object> countInfo){
        PageVo<T> pageVo = PageVo.<T>builder()
                .totalRecords(page.getTotal())
                .totalPages((int)page.getPages())
                .currentPage((int)page.getCurrent())
                .pageSize((int)page.getSize())
                .list(page.getRecords())
                .countInfo(countInfo)
                .build();
        return ResultVo.<PageVo<T>>builder()
                .code(code)
                .msg(msg)
                .data(pageVo)
                .build();

    }

 }
