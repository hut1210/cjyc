package com.cjyc.common.model.vo.salesman.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class BtnListVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "角色按钮列表")
    private String btnList;
}
