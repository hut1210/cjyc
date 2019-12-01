package com.cjyc.common.model.vo.driver.mine;

import com.cjyc.common.model.vo.FreeDriverVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CarrierDriverVo implements Serializable {
    private static final long serialVersionUID = 7690303085140845673L;

    @ApiModelProperty("承运商下空闲司机")
    private List<FreeDriverVo> driverVo;
}