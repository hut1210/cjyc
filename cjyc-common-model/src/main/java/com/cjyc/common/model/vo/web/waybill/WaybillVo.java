package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.Waybill;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WaybillVo extends Waybill {



    @ApiModelProperty("车辆列表")
    private List<TrunkDetailWaybillCarVo> list;

}
