package com.cjyc.common.model.vo.store;

import com.cjyc.common.model.entity.Store;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 业务中心列表返回VO
 * @Author Liu Xing Xiang
 * @Date 2019/11/18 14:13
 **/
@Data
public class StoreVo extends Store {
    private static final long serialVersionUID = 2104666354394337355L;
    @ApiModelProperty(value = "归属大区名称")
    private String regionName;

    @ApiModelProperty(value = "管辖范围")
    private Integer areaCount;
}
