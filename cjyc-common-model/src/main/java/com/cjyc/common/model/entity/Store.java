package com.cjyc.common.model.entity;

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
 * 韵车业务中心信息表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_store")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 城市ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;

    /**
     * 部门ID（架构组sys_dept表）
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 业务中心名称
     */
    @TableField("name")
    private String name;

    /**
     * 省
     */
    @TableField("province")
    private String province;

    /**
     * 省编码
     */
    @TableField("province_code")
    private String provinceCode;

    /**
     * 市
     */
    @TableField("city")
    private String city;

    /**
     * 市编码
     */
    @TableField("city_code")
    private String cityCode;

    /**
     * 区
     */
    @TableField("area")
    private String area;

    /**
     * 区编码
     */
    @TableField("area_code")
    private String areaCode;

    /**
     * 具体地址
     */
    @TableField("detail_addr")
    private String detailAddr;

    /**
     * 业务中心经度
     */
    @TableField("lng")
    private String lng;

    /**
     * 业务中心纬度
     */
    @TableField("lat")
    private String lat;

    /**
     * 状态：0待审核，2已开通，4已取消，7已驳回，9已停用
     */
    @TableField("state")
    private Integer state;

    /**
     * 创建人ID
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;

    /**
     * 更新人ID
     */
    @TableField("update_user_id")
    private Long updateUserId;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private String updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


}
