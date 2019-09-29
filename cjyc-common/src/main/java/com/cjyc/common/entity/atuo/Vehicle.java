package com.cjyc.common.entity.atuo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2019-09-29
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
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 车牌号
     */
    @TableField("plate_no")
    private String plateNo;

    /**
     * 车架号
     */
    @TableField("vin")
    private String vin;

    /**
     * 车位数
     */
    @TableField("default_carry_num")
    private Integer defaultCarryNum;

    /**
     * 所有权：0韵车自营，1个人所有，2第三方物流公司
     */
    @TableField("ownership_type")
    private Integer ownershipType;

    /**
     * 位置传感器：0没有，1已安装，4已关闭
     */
    @TableField("position_sensor_flag")
    private byte[] positionSensorFlag;

    /**
     * 行车记录仪：0没有，1已安装
     */
    @TableField("tachograph_flag")
    private Integer tachographFlag;

    /**
     * 状态：0待审核，2已审核，4已驳回，7已停用
     */
    @TableField("state")
    private Integer state;

    /**
     * 行驶证
     */
    @TableField("driving_license")
    private String drivingLicense;

    /**
     * 行驶证左页
     */
    @TableField("driving_license_left_img")
    private String drivingLicenseLeftImg;

    /**
     * 行驶证右页
     */
    @TableField("driving_license_right_img")
    private String drivingLicenseRightImg;

    /**
     * 行驶证过期时间
     */
    @TableField("driving_license_expire")
    private String drivingLicenseExpire;

    /**
     * 创建人ID
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
