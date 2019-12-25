package com.cjyc.common.model.entity;

import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 运单明细表(车辆表)
 * </p>
 *
 * @author JPG
 * @since 2019-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_waybill_car")
@ApiModel(value="WaybillCar对象", description="运单明细表(车辆表)")
public class WaybillCar implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "运单ID")
    private Long waybillId;

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "订单车辆ID")
    private Long orderCarId;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @Excel(name = "运费(元)", orderNum = "9")
    @ApiModelProperty(value = "运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "省")
    private String startProvince;

    @ApiModelProperty(value = "省编码")
    private String startProvinceCode;

    @ApiModelProperty(value = "市")
    private String startCity;

    @ApiModelProperty(value = "市编码")
    private String startCityCode;

    @ApiModelProperty(value = "区")
    private String startArea;

    @ApiModelProperty(value = "区县编码")
    private String startAreaCode;

    @Excel(name = "提车地址", orderNum = "17")
    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "出发地业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long startStoreId;

    @ApiModelProperty(value = "起始地所属业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long startBelongStoreId;

    @ApiModelProperty(value = "省")
    private String endProvince;

    @ApiModelProperty(value = "省编码")
    private String endProvinceCode;

    @ApiModelProperty(value = "市")
    private String endCity;

    @ApiModelProperty(value = "市编码")
    private String endCityCode;

    @ApiModelProperty(value = "区")
    private String endArea;

    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;

    @Excel(name = "交付地址", orderNum = "18")
    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @Excel(name = "提/送车业务中心", orderNum = "16")
    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endStoreId;

    @ApiModelProperty(value = "目的地所属业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endBelongStoreId;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "运单车辆状态：0待指派，2已指派，5待装车，15待装车确认，45已装车，70已卸车，90确认交车, 100确认收车, 105待重连，120已重连")
    private Integer state;

    @ApiModelProperty(value = "预计提车日期")
    private Long expectStartTime;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndTime;

    /*@Excel(name = "运载方式", orderNum = "10")
    @TableField(exist = false)
    private String takeTypeDesc;*/

    @Excel(name = "提车联系人", orderNum = "20")
    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人userid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long loadLinkUserId;

    @Excel(name = "提车联系人电话", orderNum = "21")
    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "图片地址，逗号分隔")
    private String loadPhotoImg;

    @ApiModelProperty(value = "实际开始装车时间")
    private Long loadTime;

    @Excel(name = "交付联系人", orderNum = "22")
    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long unloadLinkUserId;

    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "图片地址，逗号分隔")
    private String unloadPhotoImg;

    @ApiModelProperty(value = "实际完成卸车时间")
    private Long unloadTime;

    @ApiModelProperty(value = "最后一次运输标识：0否，1是")
    private Boolean receiptFlag;

    @Excel(name = "实际交付日期", orderNum = "11")
    @TableField(exist = false)
    private String uploadTimeDesc;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;


}
