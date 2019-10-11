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
 * 承运商积分明细表
 * </p>
 *
 * @author JPG
 * @since 2019-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_carrier_point_detail")
@ApiModel(value="CarrierPointDetail对象", description="承运商积分明细表")
public class CarrierPointDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "来源：1物流运输")
    private Integer source;

    @ApiModelProperty(value = "来源编号：任务单号")
    private String sourceNo;

    @ApiModelProperty(value = "类型：0收入，1支出")
    private Integer type;

    @ApiModelProperty(value = "积分数量")
    private Integer pointNum;

    @ApiModelProperty(value = "说明")
    private String description;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;


}
