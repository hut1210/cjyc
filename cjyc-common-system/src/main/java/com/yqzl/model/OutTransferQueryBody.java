package com.yqzl.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author:RenPL
 * @Date:2020/3/25 15:59
 */
@Data
public class OutTransferQueryBody {

    @ApiModelProperty("查询标志")
    private String queryFlag;

    @ApiModelProperty("流水号")
    private String oglSerialNo;


    public OutTransferQueryBody(String queryFlag, String oglSerialNo) {
        this.queryFlag = queryFlag;
        this.oglSerialNo = oglSerialNo;
    }

    public String getOutTransferQueryBody(OutTransferQueryBody outTransferQueryBody) {

        return "<body><query_flag>" + outTransferQueryBody.getQueryFlag() + "</query_flag>" +
                "<ogl_serial_no>" + outTransferQueryBody.getOglSerialNo() + "</ogl_serial_no></body>";
    }
}
