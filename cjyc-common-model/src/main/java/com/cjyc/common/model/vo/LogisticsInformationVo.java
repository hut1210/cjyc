package com.cjyc.common.model.vo;

import com.cjyc.common.model.vo.customer.order.OutterOrderCarLogVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 物流信息
 * @Author Liu Xing Xiang
 * @Date 2020/4/3 9:35
 **/
@Data
public class LogisticsInformationVo implements Serializable {
    private static final long serialVersionUID = -3641004003369929956L;
    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;
    @ApiModelProperty(value = "当前状态")
    private String outterState;
    @ApiModelProperty(value = "节点列表")
    List<OutterOrderCarLogVo> list;

    public String getOutterState() {
        return outterState == null ? "" : outterState;
    }
}
