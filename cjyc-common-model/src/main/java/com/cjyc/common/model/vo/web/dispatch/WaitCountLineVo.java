package com.cjyc.common.model.vo.web.dispatch;

import lombok.Data;

@Data
public class WaitCountLineVo {
    private Integer carNum;
    private String endCityCode;
    private String endCity;
    private String startCityCode;
    private String startCity;
    private String line;

    public String getStartCityCode() {
        return startCityCode == null ? "" : startCityCode;
    }

    public String getStartCity() {
        return startCity == null ? "" : startCity;
    }

    public String getEndCityCode() {
        return endCityCode == null ? "" : endCityCode;
    }

    public String getEndCity() {
        return endCity == null ? "" : endCity;
    }

    public String getLine() {
        return line == null ? "" : line;
    }
}
