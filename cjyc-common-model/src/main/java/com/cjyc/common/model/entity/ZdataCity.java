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
 * 
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zdata_city")
public class ZdataCity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @TableId(value = "code", type = IdType.INPUT)
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 上级行政区编码
     */
    private String parentCode;

    /**
     * 上级行政区名称
     */
    private String parentName;

    /**
     * 上上级行政区编码
     */
    private String grandpaCode;

    /**
     * 上上级行政区名称
     */
    private String grandpaName;

    /**
     * 级别
     */
    private Boolean level;


}
