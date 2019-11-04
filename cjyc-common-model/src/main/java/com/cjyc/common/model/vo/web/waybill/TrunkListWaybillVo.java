package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.Waybill;
import lombok.Data;

import java.util.List;

@Data
public class TrunkListWaybillVo extends Waybill {

    private List<TrunkListTaskVo> list;

}
