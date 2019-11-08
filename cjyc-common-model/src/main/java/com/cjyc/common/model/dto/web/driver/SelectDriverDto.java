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

    @ApiModelProperty("营运状态：0营运中，1停运中")
    private Integer businessState;

    @ApiModelProperty("状态：0待审核，2审核通过，4已驳回(审核不通过)，7已冻结")
    private Integer state;

    @ApiModelProperty("司机类型 0 ：代驾 1：干线司机  2：拖车司机 4全支持")
    private Integer mode;

}