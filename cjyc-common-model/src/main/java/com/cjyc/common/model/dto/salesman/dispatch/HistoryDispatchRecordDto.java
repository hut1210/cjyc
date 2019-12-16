package com.cjyc.common.model.dto.salesman.dispatch;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 历史调度记录参数对象
 * @Author Liu Xing Xiang
 * @Date 2019/12/13 17:13
 **/
@Data
public class HistoryDispatchRecordDto extends BasePageDto {
    private static final long serialVersionUID = 5067907214901627152L;
    @ApiModelProperty(value = "登录用户id",required = true)
    @NotNull(message = "登录用户id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "运单编号/承运商手机号")
    private String key;

    @ApiModelProperty(value = "运单类型：1提车，2干线运输，3送车，0全部")
    private Integer type;

    @ApiModelProperty("起始城市")
    private String startCity;

    @ApiModelProperty("目的城市")
    private String endCity;

    @ApiModelProperty(value = "调度日期开始")
    private Long createTimeS;

    @ApiModelProperty(value = "调度日期结束")
    private Long createTimeE;

    private String bizStoreIds;
}
