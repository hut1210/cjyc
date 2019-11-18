package com.cjyc.common.model.dto.web.salesman;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Description 我的业务员查询实体
 * @Author LiuXingXiang
 * @Date 2019/11/12 18:27
 **/
@ApiModel
@Data
public class MySalesmanQueryDto extends BasePageDto {
    private static final long serialVersionUID = 2276066081881754008L;
    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "业务中心ID不能为空")
    private Long roleId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}",message = "电话号码格式不对")
    private String phone;
}
