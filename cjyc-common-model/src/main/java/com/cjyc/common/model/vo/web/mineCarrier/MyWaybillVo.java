package com.cjyc.common.model.vo.web.mineCarrier;

import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class MyWaybillVo implements Serializable {
    private static final long serialVersionUID = 5499187790421129998L;

    @ApiModelProperty("运单id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long billId;

    @ApiModelProperty("运单编号")
    private String billNo;

    @ApiModelProperty("运输线路")
    private String guideLine;

    @ApiModelProperty("被分配的总车辆数")
    private Integer totalCarNum;

    @ApiModelProperty("待分配的车辆数")
    private Integer allocateCarNum;

    @ApiModelProperty("承运商名称")
    private String carrierName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty("创建人")
    private String createUser;
}