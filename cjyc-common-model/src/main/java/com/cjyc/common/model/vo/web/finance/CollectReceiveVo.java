package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hut
 * @Date: 2019/12/16 15:23
 */
@Data
public class CollectReceiveVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "运单单号")
    private String wayBillNo;

    @ApiModelProperty(value = "运单类型")
    private int wayBillType;

    @ApiModelProperty(value = "代收款时间")
    private Long  collectReceiveTime;

    @ApiModelProperty(value = "代收款人")
    private String  collectReceiveMan;

    @ApiModelProperty(value = "代收款电话")
    private String  collectReceivePhone;

    @ApiModelProperty(value = "备注")
    private String  remark;

    @ApiModelProperty(value = "回款状态")
    private String  state;

}
