package com.cjyc.common.model.vo.web.store;

import com.cjyc.common.model.entity.Store;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 业务中心列表返回VO
 * @Author Liu Xing Xiang
 * @Date 2019/11/18 14:13
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class StoreVo extends Store {
    private static final long serialVersionUID = 2104666354394337355L;
    @ApiModelProperty(value = "归属大区名称")
    private String regionName;

    @ApiModelProperty(value = "管辖范围")
    private Integer areaCount;

    @ApiModelProperty(value = "联系人手机号")
    private String contactAdminPhone;

    @ApiModelProperty(value = "联系人手机号")
    private String contactAdmin;
}
