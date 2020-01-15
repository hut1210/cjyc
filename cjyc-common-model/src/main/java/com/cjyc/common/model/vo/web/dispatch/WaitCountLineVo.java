package com.cjyc.common.model.vo.web.dispatch;

import lombok.Data;

@Data
public class WaitCountLineVo {
    private Integer carCount;
    private String endCityCode;
    private String endCity;
    private String line;
}
