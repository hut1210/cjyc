package com.yqzl.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author:RenPL
 * @Date:2020/3/25 15:59
 */
@Data
public class OutAccountQueryBody {

    @ApiModelProperty("账号")
    private String acno;


    public OutAccountQueryBody(String acno) {
        this.acno = acno;
    }

    public String getOutAccountQueryBody(OutAccountQueryBody outAccountQueryBody) {

        return "<body><acno>" + outAccountQueryBody.getAcno() + "</acno></body>";
    }
}
