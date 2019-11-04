package com.cjyc.common.model.dto.web.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 业务中心新增实体
 * @Author LiuXingXiang
 * @Date 2019/10/29 16:34
 **/
@Data
public class StoreAddDto implements Serializable {
    private static final long serialVersionUID = -7690545988224110399L;
    @ApiModelProperty(value = "部门ID",required = true)
//    @NotNull(message = "部门ID不能为空")
    private Long deptId;

    @ApiModelProperty(value = "业务中心名称",required = true)
    @NotBlank(message = "业务中心名称不能为空")
    private String name;

    @ApiModelProperty(value = "省",required = true)
    @NotBlank(message = "省不能为空")
    private String province;

    @ApiModelProperty(value = "省编码",required = true)
    @NotBlank(message = "省编码不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "市",required = true)
    @NotBlank(message = "市不能为空")
    private String city;

    @ApiModelProperty(value = "市编码",required = true)
    @NotBlank(message = "市编码不能为空")
    private String cityCode;

    @ApiModelProperty(value = "区",required = true)
    @NotBlank(message = "区不能为空")
    private String area;

    @ApiModelProperty(value = "区编码",required = true)
    @NotBlank(message = "区编码不能为空")
    private String areaCode;

    @ApiModelProperty(value = "具体地址",required = true)
    @NotBlank(message = "具体地址不能为空")
    private String detailAddr;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID",required = true)
    @NotNull(message = "创建人ID不能为空")
    private Long createUserId;
}
