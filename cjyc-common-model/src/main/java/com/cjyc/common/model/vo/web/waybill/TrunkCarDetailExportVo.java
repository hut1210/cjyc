package com.cjyc.common.model.vo.web.waybill;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 运单明细-干线 导出实体
 */
@Data
public class TrunkCarDetailExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "运单ID")
    private Long waybillId;

    @ApiModelProperty(value = "运单编号")
    @Excel(name = "运单编号", orderNum = "0",width = 20)
    private String waybillNo;

    @ApiModelProperty(value = "订单车辆ID")
    private Long orderCarId;

    @ApiModelProperty(value = "车辆编号")
    @Excel(name = "车辆编号", orderNum = "1",width = 20)
    private String orderCarNo;

    @ApiModelProperty(value = "运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @Excel(name = "下游运费(元)", orderNum = "5",width = 15)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "省")
    private String startProvince;

    @ApiModelProperty(value = "省编码")
    private String startProvinceCode;

    @ApiModelProperty(value = "市")
    @Excel(name = "订单始发地", orderNum = "6",width = 20)
    private String startCity;

    @ApiModelProperty(value = "市编码")
    private String startCityCode;

    @ApiModelProperty(value = "区")
    private String startArea;

    @ApiModelProperty(value = "区县编码")
    private String startAreaCode;

    @ApiModelProperty(value = "装车地址")
    @Excel(name = "提车地址", orderNum = "17",width = 20)
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心名称")
    @Excel(name = "提车业务中心", orderNum = "14",width = 20)
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
    @Excel(name = "订单目的地", orderNum = "7",width = 15)
    private String endCity;

    @ApiModelProperty(value = "市编码")
    private String endCityCode;

    @ApiModelProperty(value = "区")
    private String endArea;

    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;

    @ApiModelProperty(value = "卸车地址")
    @Excel(name = "交付地址", orderNum = "23",width = 20)
    private String endAddress;

    @ApiModelProperty(value = "目的地业务中心名称")
    @Excel(name = "交付业务中心", orderNum = "20",width = 20)
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


    @ApiModelProperty(value = "提车联系人")
    @Excel(name = "提车联系人", orderNum = "15",width = 15)
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人userid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long loadLinkUserId;

    @ApiModelProperty(value = "提车联系人电话")
    @Excel(name = "提车电话", orderNum = "16",width = 15)
    private String loadLinkPhone;

    @ApiModelProperty(value = "图片地址，逗号分隔")
    private String loadPhotoImg;

    @ApiModelProperty(value = "实际开始装车时间")
    private Long loadTime;

    @ApiModelProperty(value = "收车人名称")
    @Excel(name = "交付联系人", orderNum = "21",width = 20)
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long unloadLinkUserId;

    @ApiModelProperty(value = "收车人电话")
    @Excel(name = "交付电话", orderNum = "22",width = 15)
    private String unloadLinkPhone;

    @ApiModelProperty(value = "图片地址，逗号分隔")
    private String unloadPhotoImg;

    @ApiModelProperty(value = "实际完成卸车时间")
    private Long unloadTime;

    @ApiModelProperty(value = "最后一次运输标识：0否，1是")
    private Boolean receiptFlag;

    @TableField(exist = false)
    private String uploadTimeDesc;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "订单编号")
    @Excel(name = "订单编号", orderNum = "11",width = 15)
    private String orderNo;
    @ApiModelProperty(value = "状态")
    @Excel(name = "状态", orderNum = "10",width = 15)
    private String outterState;

    @ApiModelProperty(value = "承运商ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty(value = "(carryType)承运类型：1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己")
    private Integer carrierType;

    @Excel(name = "承运商", orderNum = "24",width = 20)
    private String carrierName;


    @ApiModelProperty(value = "调度人")
    @Excel(name = "创建人", orderNum = "29",width = 20)
    private String createUser;

    @ApiModelProperty(value = "调度人ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    @ApiModelProperty(value = "品牌")
    @Excel(name = "品牌", orderNum = "3",width = 15)
    private String brand;

    @ApiModelProperty(value = "型号")
    @Excel(name = "车系", orderNum = "4",width = 15)
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    @Excel(name = "VIN码", orderNum = "2",width = 20)
    private String vin;

    @ApiModelProperty(value = "司机名称")
    @Excel(name = "司机", orderNum = "25",width = 20)
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    @Excel(name = "司机电话", orderNum = "26",width = 20)
    private String driverPhone;

    @ApiModelProperty(value = "司机ID(loginId)")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty(value = "运力车牌号")
    @Excel(name = "车牌号", orderNum = "27",width = 20)
    private String vehiclePlateNo;
    @Excel(name = "司机提车地", orderNum = "8",width = 20)
    private String pickDetailAddr;
    @Excel(name = "司机交付地", orderNum = "9",width = 20)
    private String backDetailAddr;
    @Excel(name = "提车日期", orderNum = "12",width = 20)
    private String expectStartTimeStr;
    @Excel(name = "实际提车日期", orderNum = "13",width = 20)
    private String loadTimeStr;
    @Excel(name = "预计到达日期", orderNum = "18",width = 20)
    private String expectEndTimeStr;
    @Excel(name = "实际交付日期", orderNum = "19",width = 20)
    private String unloadTimeStr;
    @Excel(name = "创建日期", orderNum = "28",width = 20)
    private String createTimeStr;

    public String getCreateTimeStr() {
        Long date = getCreateTime();
        if(null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.DATETIME);
    }

    public String getUnloadTimeStr() {
        Long date = getUnloadTime();
        if(null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.DATE);
    }

    public String getExpectEndTimeStr() {
        Long date = getExpectEndTime();
        if(null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.DATE);
    }

    public String getLoadTimeStr() {
        Long date = getLoadTime();
        if(null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.DATE);
    }

    public String getExpectStartTimeStr() {
        Long date = getExpectStartTime();
        if(null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.DATE);
    }

    public String getBackDetailAddr() {
        return (StringUtils.isEmpty(getEndProvince())?"": getEndProvince()) + "/" +
                (StringUtils.isEmpty(getEndCity())?"": getEndCity()) + "/" +
                (StringUtils.isEmpty(getEndArea())?"": getEndArea());
    }
    public String getPickDetailAddr() {
        return (StringUtils.isEmpty(getStartProvince())?"": getStartProvince()) + "/" +
                (StringUtils.isEmpty(getStartCity())?"": getStartCity()) + "/" +
                (StringUtils.isEmpty(getStartArea())?"": getStartArea());
    }
}
