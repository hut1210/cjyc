package com.cjyc.common.model.vo.web.handleData;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class LoginAppExcelVo implements Serializable {
    private static final long serialVersionUID = 3028598227200942813L;

    @ApiModelProperty(value = "姓名")
    @Excel(name = "姓名", orderNum = "0")
    private String name;

    @ApiModelProperty(value = "登录账号")
    @Excel(name = "登录账号", orderNum = "1")
    private String phone;
}