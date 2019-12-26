package com.cjyc.common.model.dto.web.driver;

import com.cjyc.common.model.dto.BasePageDto;
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
public class SelectDriverDto extends BasePageDto implements Serializable {

    @ApiModelProperty("登陆用户id(loginId)")
    private Long loginId;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机电话")
    private String phone;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("司机身份 0：普通司机 1：管理员")
    private Integer identity;

    @ApiModelProperty("营运状态：0营运中(空闲)，1停运中(繁忙)")
    private Integer businessState;

    @ApiModelProperty("状态：0待审核，2已审核，4取消，5冻结  7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线  4：拖车")
    private Integer mode;

}