package com.cjyc.common.model.vo.web.order;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ListOrderVo extends Order {
    @ApiModelProperty("状态")
    private String outterState;
    @ApiModelProperty("出发地址（全）")
//    @Excel(name = "出发地址（全）", orderNum = "0")
    private String startFullAddress;
    @ApiModelProperty("目的地址（全）")
//    @Excel(name = "目的地址（全）", orderNum = "1")
    private String endFullAddress;

    @ApiModelProperty("总物流费")
    @Excel(name = "总费用(元)", orderNum = "7")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal wlTotalFee;
    @ApiModelProperty("大区")
    @Excel(name = "归属大区", orderNum = "4")
    private String region;
    @ApiModelProperty("大区编码")
    @Excel(name = "大区编码", orderNum = "4")
    private String regionCode;
    @ApiModelProperty("所属业务中心地址")
    @Excel(name = "所属业务中心地址", orderNum = "5")
    private String inputStoreAddress;
    @ApiModelProperty("出发业务中心地址")
    @Excel(name = "收车业务中心地址", orderNum = "16")
    private String startStoreAddress;
    @ApiModelProperty("目的业务中心地址")
    @Excel(name = "送车业务中心地址", orderNum = "18")
    private String endStoreAddress;
    @ApiModelProperty("合同编号")
    @Excel(name = "合同编号", orderNum = "12")
    private String contractNo;
    @ApiModelProperty("账期/天")
    @Excel(name = "账期/天", orderNum = "9")
    private String settlePeriod;
    @ApiModelProperty("总服务费")
    @Excel(name = "合伙人服务费(元)", orderNum = "8")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalAgencyFee;
    @Excel(name = "订单状态", orderNum = "1")
    private String stateStr;
    @Excel(name = "订单来源", orderNum = "3")
    private String sourceStr;
    @Excel(name = "支付方式", orderNum = "5")
    private String payTypeStr;
    @Excel(name = "客户类型", orderNum = "9")
    private String customerTypeStr;
    @Excel(name = "提车日期", orderNum = "19")
    private String expectStartDateStr;
    @Excel(name = "提车方式", orderNum = "20")
    private String pickTypeStr;
    @Excel(name = "预计到达", orderNum = "24")
    private String expectEndDateStr;
    @Excel(name = "交付方式", orderNum = "25")
    private String backTypeStr;
    @Excel(name = "下单时间", orderNum = "30")
    private String createTimeStr;
    @Excel(name = "接单时间", orderNum = "32")
    private String checkTimeStr;


    public String getCheckTimeStr() {
        Long date = getCheckTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }

    public String getCreateTimeStr() {
        Long date = getCreateTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }

    public String getBackTypeStr() {
        Integer backType = getBackType();
        if (null == backType) {
            return "";
        }
        String str = "";
        switch (backType) {
            case 1:
                str = "自提"; break;
            case 2:
                str = "代驾上门"; break;
            case 3:
                str = "拖车上门"; break;
            case 4:
                str = "物流上门"; break;
        }
        return str;
    }

    public String getExpectEndDateStr() {
        Long date = getExpectEndDate();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }

    public String getPickTypeStr() {
        Integer pickType = getPickType();
        if (null == pickType) {
            return "";
        }
        String str = "";
        switch (pickType) {
            case 1:
                str = "自送"; break;
            case 2:
                str = "代驾上门"; break;
            case 3:
                str = "拖车上门"; break;
            case 4:
                str = "物流上门"; break;
        }
        return str;
    }

    public String getExpectStartDateStr() {
        Long date = getExpectStartDate();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }

    public String getCustomerTypeStr() {
        Integer customerType = getCustomerType();
        if (null == customerType) {
            return "";
        }
        String str = "";
        switch (customerType) {
            case 1:
                str = "个人"; break;
            case 2:
                str = "企业"; break;
            case 3:
                str = "合伙人"; break;
        }
        return str;
    }

    public String getPayTypeStr() {
        Integer payType = getPayType();
        if (null == payType) {
            return "";
        }
        String str = "";
        switch (payType) {
            case 0:
                str = "到付"; break;
            case 1:
                str = "预付"; break;
            case 2:
                str = "账期"; break;
        }
        return str;
    }

    public String getSourceStr() {
        Integer source = getSource();
        if (null == source) {
            return "";
        }
        String str = "";
        switch (source) {
            case 1:
                str = "WEB管理后台"; break;
            case 2:
                str = "业务员APP"; break;
            case 4:
                str = "司机APP"; break;
            case 6:
                str = "用户端APP"; break;
            case 7:
                str = "用户端小程序"; break;
        }
        return str;
    }

    public String getStateStr() {
        Integer state = getState();
        if (null == state) {
            return "";
        }
        String str = "";
        switch (state) {
            case 0:
                str = "待提交"; break;
            case 2:
                str = "待分配"; break;
            case 5:
                str = "待确认"; break;
            case 10:
                str = "待复确认"; break;
            case 15:
                str = "待预付款"; break;
            case 25:
                str = "已确认"; break;
            case 55:
                str = "运输中"; break;
            case 88:
                str = "待付款"; break;
            case 100:
                str = "已完成"; break;
            case 111:
                str = "原返（待）"; break;
            case 112:
                str = "异常结束"; break;
            case 113:
                str = "取消（待）"; break;
            case 114:
                str = "作废（待）"; break;
        }
        return str;
    }
}
