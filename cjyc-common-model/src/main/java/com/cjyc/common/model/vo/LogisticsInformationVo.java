package com.cjyc.common.model.vo;

import com.cjyc.common.model.vo.customer.order.OutterLogVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 物流信息
 * @Author Liu Xing Xiang
 * @Date 2020/4/3 9:35
 **/
@Data
public class LogisticsInformationVo extends OutterLogVo{
    private static final long serialVersionUID = -3641004003369929956L;
    @ApiModelProperty(value = "车辆实时位置")
    private String location;

    public String getLocation() {
        return location == null ? "" : location;
    }
}
