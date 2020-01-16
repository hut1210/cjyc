package com.cjyc.common.model.vo.web.dispatch;

import lombok.Data;

import java.util.List;

@Data
public class WaitCountVo {

    private Integer carNum;
    private String startCityCode;
    private String startCity;
    private List<WaitCountLineVo> list;

    public String getStartCityCode() {
        return startCityCode == null ? "" : startCityCode;
    }

    public String getStartCity() {
        return startCity == null ? "" : startCity;
    }

}
