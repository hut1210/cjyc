package com.cjyc.common.model.entity.auto;

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
 * 客户常用联系人表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_customer_contact")
public class CustomerContact implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 客户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

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
     * 详细地址
     */
    @TableField("detail_addr")
    private String detailAddr;

    /**
     * 备注
     */
    @TableField("rewark")
    private String rewark;

    /**
     * 状态：0无效，1有效
     */
    @TableField("state")
    private Integer state;


}
