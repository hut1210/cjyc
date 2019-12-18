package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Hut
 * @Date: 2019/12/17 16:07
 * 现金结算明细
 */
@Data
public class CashSettlementDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "车辆明细")
    List<CashCarDetailVo> cashCarDetailVoList;

    @ApiModelProperty(value = "代收款人")
    private String  collectReceiveMan;

    @ApiModelProperty(value = "代收款电话")
    private String  collectReceivePhone;

    @ApiModelProperty(value = "代收款金额")
    private String  collectReceiveMoney;

    @ApiModelProperty(value = "备注")
    private String  remark;

}
