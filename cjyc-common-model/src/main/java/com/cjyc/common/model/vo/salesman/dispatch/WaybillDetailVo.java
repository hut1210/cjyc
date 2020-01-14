package com.cjyc.common.model.vo.salesman.dispatch;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DateLongSerizlizer;
import com.cjyc.common.model.vo.driver.task.CarDetailVo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 运单明细VO
 * @Author Liu Xing Xiang
 * @Date 2019/12/16 16:09
 **/
@Data
public class WaybillDetailVo implements Serializable {
    private static final long serialVersionUID = 1170810595535938015L;
    @ApiModelProperty(value = "承运商联系人")
    private String carrierName;

    @ApiModelProperty(value = "承运商手机号")
    private String linkmanPhone;

    @ApiModelProperty(value = "运单编号")
    private String no;

    @ApiModelProperty(value = "调度日期")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty(value = "运单类型")
    private Integer type;

    @ApiModelProperty(value = "运单状态：")
    private Integer state;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "运单运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "车辆信息列表")
    private List<CarDetailVo> carDetailVoList;

    public String getGuideLine() {
        return guideLine == null ? "" : guideLine;
    }
}
