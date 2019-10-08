package com.cjyc.common.model.vo;

import lombok.Data;

@Data
public class CityVo {

    private static final long serialVersionUID = 1L;

    /**
     * 行政区编码（含大区、省、市、区）
     */
    private String code;

    /**
     * 上级城市编码
     */
    private String parentCode;

    /**
     * 行政区名称
     */
    private String name;

    /**
     * 行政区级别： 0大区， 1省， 2市， 3区县
     */
    private Boolean level;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 热门城市： 0否， 1是
     */
    private Boolean hot;

    private Long createTime;

    private Long updateTime;


}
