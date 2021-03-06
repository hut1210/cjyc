package com.cjyc.common.model.vo.web.order;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.MoneyUtil;
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
    @Excel(name = "提车地址", orderNum = "24", width = 20)
    private String startFullAddress;
    @ApiModelProperty("目的地址（全）")
    @Excel(name = "交付地址", orderNum = "29", width = 20)
    private String endFullAddress;


    @ApiModelProperty("总物流费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal wlTotalFee;
    @ApiModelProperty("大区")
    @Excel(name = "归属大区", orderNum = "4", width = 10)
    private String region;
    @ApiModelProperty("大区编码")
    private String regionCode;
    @ApiModelProperty("所属业务中心地址")
    private String inputStoreAddress;
    @ApiModelProperty("出发业务中心地址")
    @Excel(name = "收车业务中心地址", orderNum = "17", width = 20)
    private String startStoreAddress;
    @ApiModelProperty("目的业务中心地址")
    @Excel(name = "送车业务中心地址", orderNum = "19", width = 20)
    private String endStoreAddress;
    @ApiModelProperty("合同编号")
    @Excel(name = "合同编号", orderNum = "13", width = 15)
    private String contractNo;
    @ApiModelProperty("账期/天")
    private String settlePeriod;
    @ApiModelProperty("总服务费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalAgencyFee;
    @Excel(name = "订单状态", orderNum = "1")
    private String stateStr;
    @Excel(name = "订单来源", orderNum = "3", width = 15)
    private String sourceStr;
    @Excel(name = "支付方式", orderNum = "6", width = 10)
    private String payTypeStr;
    @Excel(name = "客户类型", orderNum = "10", width = 10)
    private String customerTypeStr;
    @Excel(name = "提车日期", orderNum = "20", width = 20)
    private String expectStartDateStr;
    @Excel(name = "提车方式", orderNum = "21", width = 10)
    private String pickTypeStr;
    @Excel(name = "预计到达", orderNum = "25", width = 20)
    private String expectEndDateStr;
    @Excel(name = "交付方式", orderNum = "26", width = 15)
    private String backTypeStr;
    @Excel(name = "下单时间", orderNum = "31", width = 20)
    private String createTimeStr;
    @Excel(name = "接单时间", orderNum = "33", width = 20)
    private String checkTimeStr;
    @Excel(name = "订单金额(元)", orderNum = "7", type = 10, width = 15)
    private String totalFeeStr;
    @Excel(name = "总费用(元)", orderNum = "8", type = 10, width = 10)
    private String wlTotalFeeStr;
    @Excel(name = "合伙人服务费(元)", orderNum = "9", type = 10, width = 15)
    private String totalAgencyFeeStr;

    public String getTotalFeeStr() {
        return MoneyUtil.fenToYuan(getTotalFee(), MoneyUtil.PATTERN_TWO);
    }

    public String getWlTotalFeeStr() {
        return MoneyUtil.fenToYuan(getWlTotalFee(), MoneyUtil.PATTERN_TWO);
    }

    public String getTotalAgencyFeeStr() {
        return MoneyUtil.fenToYuan(getTotalAgencyFee(), MoneyUtil.PATTERN_TWO);
    }

    public String getCheckTimeStr() {
        Long date = getCheckTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.DATETIME);
    }


    public String getCreateTimeStr() {
        Long date = getCreateTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.DATETIME);
    }


    public String getBackTypeStr() {
        Integer backType = getBackType();
        if (null == backType) {
            return "";
        }
        String str = "";
        switch (backType) {
            case 1:
                str = "自提";
                break;
            case 2:
                str = "代驾上门";
                break;
            case 3:
                str = "拖车上门";
                break;
            case 4:
                str = "物流上门";
                break;
        }
        return str;
    }

    public String getExpectEndDateStr() {
        Long date = getExpectEndDate();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.DATETIME);
    }

    public String getPickTypeStr() {
        Integer pickType = getPickType();
        if (null == pickType) {
            return "";
        }
        String str = "";
        switch (pickType) {
            case 1:
                str = "自送";
                break;
            case 2:
                str = "代驾上门";
                break;
            case 3:
                str = "拖车上门";
                break;
            case 4:
                str = "物流上门";
                break;
        }
        return str;
    }

    public String getExpectStartDateStr() {
        Long date = getExpectStartDate();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.DATETIME);
    }

    public String getCustomerTypeStr() {
        Integer customerType = getCustomerType();
        if (null == customerType) {
            return "";
        }
        String str = "";
        switch (customerType) {
            case 1:
                str = "C端";
                break;
            case 2:
                str = "大客户";
                break;
            case 3:
                str = "合伙人";
                break;
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
                str = "到付";
                break;
            case 1:
                str = "预付";
                break;
            case 2:
                str = "账期";
                break;
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
                str = "WEB管理后台";
                break;
            case 2:
                str = "业务员APP";
                break;
            case 4:
                str = "司机APP";
                break;
            case 6:
                str = "用户端APP";
                break;
            case 7:
                str = "用户端小程序";
                break;
            case 9:
                str = "99车圈";
                break;
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
                str = "预订单";
                break;
            case 2:
                str = "待确认";
                break;
            case 5:
                str = "待确认";
                break;
            case 10:
                str = "待复确认";
                break;
            case 15:
                str = "待付款";
                break;
            case 25:
                str = "待调度";
                break;
            case 55:
                str = "运输中";
                break;
            case 88:
                str = "待付款";
                break;
            case 100:
                str = "已交付";
                break;
            case 112:
                str = "异常结束";
                break;
            case 113:
                str = "已取消";
                break;
            case 114:
                str = "已作废";
                break;
        }
        return str;
    }
}
