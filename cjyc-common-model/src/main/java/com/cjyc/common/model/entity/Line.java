package com.cjyc.common.model.entity;

import java.math.BigDecimal;
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
 * 班线管理
 * </p>
 *
 * @author JPG
 * @since 2020-02-18
 */
@Data
@Accessors(chain = true)
@TableName("s_line")
@ApiModel(value="Line对象", description="班线管理")
public class Line implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "线路ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "线路编号")
    private String code;

    @ApiModelProperty(value = "线路名称")
    private String name;

    @ApiModelProperty(value = "出发地省名称")
    private String fromProvince;

    @ApiModelProperty(value = "出发地省编码")
    private String fromProvinceCode;

    @ApiModelProperty(value = "出发地城市名称")
    private String fromCity;

    @ApiModelProperty(value = "出发地城市编码")
    private String fromCode;

    @ApiModelProperty(value = "目的地省名称")
    private String toProvince;

    @ApiModelProperty(value = "目的地省编码")
    private String toProvinceCode;

    @ApiModelProperty(value = "目的地城市名称")
    private String toCity;

    @ApiModelProperty(value = "目的地城市编码")
    private String toCode;

    @ApiModelProperty(value = "级别：2市级，3区级")
    private Integer level;

    @ApiModelProperty(value = "总理程(KM)")
    private BigDecimal kilometer;

    @ApiModelProperty(value = "总耗时(天)")
    private BigDecimal days;

    @ApiModelProperty(value = "状态:1启用 2停用")
    private Integer state;

    @ApiModelProperty(value = "默认物流费（上游），单位分")
    private BigDecimal defaultWlFee;

    @ApiModelProperty(value = "默认运费（下游），单位分")
    private BigDecimal defaultFreightFee;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "更新人ID")
    private Long updateUserId;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;


}
