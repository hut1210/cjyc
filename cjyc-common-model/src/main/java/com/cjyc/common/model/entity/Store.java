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
 * 韵车业务中心信息表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 部门ID（架构组sys_dept表）
     */
    private Long deptId;

    /**
     * 业务中心名称
     */
    private String name;

    /**
     * 省
     */
    private String province;

    /**
     * 省编码
     */
    private String provinceCode;

    /**
     * 市
     */
    private String city;

    /**
     * 市编码
     */
    private String cityCode;

    /**
     * 区
     */
    private String area;

    /**
     * 区编码
     */
    private String areaCode;

    /**
     * 具体地址
     */
    private String detailAddr;

    /**
     * 业务中心经度
     */
    private String lng;

    /**
     * 业务中心纬度
     */
    private String lat;

    /**
     * 状态：0待审核，2已开通，4已取消，7已驳回，9已停用
     */
    private Integer state;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 备注
     */
    private String remark;


}
