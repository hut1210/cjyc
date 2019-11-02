package com.cjyc.common.model.dto.web.store;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description 业务中心分页查询实体
 * @Author LiuXingXiang
 * @Date 2019/10/29 16:17
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StoreQueryDto extends BasePageDto {
    private static final long serialVersionUID = -9002096161059047794L;
    @ApiModelProperty(value = "业务中心名称")
    private String name;

    @ApiModelProperty(value = "省编码")
    private String provinceCode;

    @ApiModelProperty(value = "市编码")
    private String cityCode;

    @ApiModelProperty(value = "区编码")
    private String areaCode;
}
