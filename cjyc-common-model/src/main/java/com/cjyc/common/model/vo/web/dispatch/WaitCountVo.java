package com.cjyc.common.model.vo.web.dispatch;

import lombok.Data;

import java.util.List;

@Data
public class WaitCountVo {
    private Integer totalCarCount;
    private String startCityCode;
    private String startCity;
    private List<WaitCountLineVo> list;

}
