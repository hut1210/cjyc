package com.cjyc.common.model.vo;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 分页&统计
 * {@link ResultVo data}数据结构
 * @author JPG
 */
@Builder
@Getter
public class PageVo<T> implements Serializable {

    private static final long serialVersionUID = 2L;
    /**总记录**/
    private long totalRecords;
    /**显示条数**/
    private int pageSize;
    /**当前页数**/
    private int currentPage;
    /**总页数**/
    private int totalPages;
    /**内容*/
    private List<T> list;
    /**非分页相关统计信息*/
    private Map<String, Object> countInfo;

}
