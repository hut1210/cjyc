package com.cjyc.common.model.vo.store;

import com.cjyc.common.model.entity.defined.FullCity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 业务中心覆盖区VO
 * @Author Liu Xing Xiang
 * @Date 2019/11/18 16:57
 **/
@Data
public class StoreCoveredAreaVo implements Serializable {
    private static final long serialVersionUID = 2221730663128512831L;
    @ApiModelProperty(value = "业务中心覆盖区域")
    private List<FullCity> coveredAreaList;

    @ApiModelProperty(value = "业务中心未覆盖的区域")
    private List<FullCity> fullCityList;
}
