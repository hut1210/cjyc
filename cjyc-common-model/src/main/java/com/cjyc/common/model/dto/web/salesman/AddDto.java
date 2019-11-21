package com.cjyc.common.model.dto.web.salesman;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 业务员添加dto
 */
@Data
@ApiModel
@Validated
public class AddDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    @NotNull(message = "用户账号不能为空")
    @Pattern(regexp = "[a-zA-Z_0-9]{6,11}",
            message = "账号为字母数字或下划线，且长度为6到11位")
    @ApiModelProperty(value = "账号", required = true)
    private String account;
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "1\\d{10}",
        message = "手机号为11位数字")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;
//    @NotNull(message = "所属机构不能为空")
    @ApiModelProperty(value = "机构标识")
    private Long deptId;
    @ApiModelProperty(value = "用户记录标识：更新用户信息时此字段必填")
    private Long id;
    @ApiModelProperty(value = "座机号")
    private String tel;
    @ApiModelProperty(value = "编号")
    private String no;
    @ApiModelProperty(value = "职位类型: 1提送车业务员，2业务员，3调度员，4客服，81财务")
    private Integer type;
    @Pattern(regexp = "\\d{6}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]"
        ,message = "身份证格式不正确，请检查")
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
    @ApiModelProperty(value = "头像图片路径")
    private String photoImg;
    @ApiModelProperty(value = "上级id")
    private Long leaderId;
    @ApiModelProperty(value = "所属业务中心id")
    private Long storeId;
    @ApiModelProperty(value = "用户状态：0审核中，2在职，4已驳回，7已离职")
    private Integer state;
}
