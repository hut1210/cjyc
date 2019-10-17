package com.cjyc.common.model.vo.web.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * vo
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class DriverListVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "user_id(查询架构组数据时使用)")
    private Long userId;

    @ApiModelProperty(value = "昵称")
    private String name;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "类型：0业务员，1自营司机，2社会司机")
    private Integer type;

    @ApiModelProperty(value = "承运方式：1代驾，2托运，3全支持")
    private Integer mode;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "公司名称")
    private String carrierName;
    @ApiModelProperty(value = "承运商类型：1个人承运商，2企业承运商")
    private String carrierType;
    @ApiModelProperty(value = "结算方式：1时付，2账期")
    private String settleType;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "运行状态：0空闲，1在途")
    private String runningState;


}
