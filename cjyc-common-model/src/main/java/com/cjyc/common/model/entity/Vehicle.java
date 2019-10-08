package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 运输车辆表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_vehicle")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 车牌号
     */
    private String plateNo;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 车位数
     */
    private Integer defaultCarryNum;

    /**
     * 所有权：0韵车自营，1个人所有，2第三方物流公司
     */
    private Integer ownershipType;

    /**
     * 位置传感器：0没有，1已安装，4已关闭
     */
    private byte[] positionSensorFlag;

    /**
     * 行车记录仪：0没有，1已安装
     */
    private Integer tachographFlag;

    /**
     * 状态：0待审核，2已审核，4已驳回，7已停用
     */
    private Integer state;

    /**
     * 行驶证
     */
    private String drivingLicense;

    /**
     * 行驶证左页
     */
    private String drivingLicenseLeftImg;

    /**
     * 行驶证右页
     */
    private String drivingLicenseRightImg;

    /**
     * 行驶证过期时间
     */
    private String drivingLicenseExpire;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Long createTime;


}
