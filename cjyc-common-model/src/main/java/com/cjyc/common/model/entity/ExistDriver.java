package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存个人到承运商下司机记录
 * </p>
 *
 * @author JPG
 * @since 2019-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_exist_driver")
@ApiModel(value="ExistDriver对象", description="保存个人到承运商下司机记录")
public class ExistDriver implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "司机id")
    private Long driverId;

    @ApiModelProperty(value = "司机姓名")
    private String name;

    @ApiModelProperty(value = "司机手机号")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "已存在身份证号")
    private String existIdCard;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;


}
