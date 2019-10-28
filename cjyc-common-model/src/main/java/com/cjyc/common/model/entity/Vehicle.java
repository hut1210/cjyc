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
 * 运输车辆表
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_vehicle")
@ApiModel(value="Vehicle对象", description="运输车辆表")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "车位数")
    private Integer defaultCarryNum;

    @ApiModelProperty(value = "所有权：0韵车自营，1个人所有，2第三方物流公司")
    private Integer ownershipType;

    @ApiModelProperty(value = "位置传感器：0没有，1已安装，4已关闭")
    private byte[] positionSensorFlag;

    @ApiModelProperty(value = "行车记录仪：0没有，1已安装")
    private Integer tachographFlag;

    @ApiModelProperty(value = "状态：0待审核，2已审核，4已驳回，7已停用")
    private Integer state;

    @ApiModelProperty(value = "行驶证")
    private String drivingLicense;

    @ApiModelProperty(value = "行驶证左页")
    private String drivingLicenseLeftImg;

    @ApiModelProperty(value = "行驶证右页")
    private String drivingLicenseRightImg;

    @ApiModelProperty(value = "行驶证过期时间")
    private String drivingLicenseExpire;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建人姓名")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;


}
