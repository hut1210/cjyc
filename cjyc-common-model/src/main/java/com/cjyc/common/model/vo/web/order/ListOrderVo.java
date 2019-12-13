package com.cjyc.common.model.vo.web.order;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.entity.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ListOrderVo extends Order {

    @ApiModelProperty("出发地址（全）")
    @Excel(name = "出发地址（全）", orderNum = "0")
    private String startFullAddress;
    @ApiModelProperty("目的地址（全）")
    @Excel(name = "目的地址（全）", orderNum = "1")
    private String endFullAddress;

    @ApiModelProperty("总物流费")
    @Excel(name = "总物流费", orderNum = "2")
    private String wlTotalFee;
    @ApiModelProperty("大区")
    @Excel(name = "大区", orderNum = "3")
    private String region;
    @ApiModelProperty("大区编码")
    @Excel(name = "大区编码", orderNum = "4")
    private String regionCode;
    @ApiModelProperty("所属业务中心地址")
    @Excel(name = "所属业务中心地址", orderNum = "5")
    private String inputStoreAddress;
    @ApiModelProperty("出发业务中心地址")
    @Excel(name = "出发业务中心地址", orderNum = "6")
    private String startStoreAddress;
    @ApiModelProperty("目的业务中心地址")
    @Excel(name = "目的业务中心地址", orderNum = "7")
    private String endStoreAddress;
    @ApiModelProperty("合同编号")
    @Excel(name = "合同编号", orderNum = "8")
    private String contractNo;
    @ApiModelProperty("账期/天")
    @Excel(name = "账期/天", orderNum = "9")
    private String settlePeriod;
    @ApiModelProperty("总服务费")
    @Excel(name = "总服务费", orderNum = "10")
    private String totalAgencyFee;


}
