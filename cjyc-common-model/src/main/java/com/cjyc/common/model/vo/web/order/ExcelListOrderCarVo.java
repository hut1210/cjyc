package com.cjyc.common.model.vo.web.order;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.MoneyUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ExcelListOrderCarVo implements Serializable {
    private static final long serialVersionUID = -3419537142436486450L;

    @Excel(name = "车辆编码", orderNum = "0", width = 20)
    private String no;
    @Excel(name = "vin码", orderNum = "1", width = 20)
    private String vin;
    @Excel(name = "付款状态", orderNum = "2", width = 15)
    private Integer wlPayState;
    @Excel(name = "品牌", orderNum = "3", width = 15)
    private String brand;
    @Excel(name = "车系", orderNum = "4", width = 15)
    private String model;
    @Excel(name = "实收总运费(元)", orderNum = "5", width = 15, type = 10)
    private BigDecimal totalFee;
    @Excel(name = "订单编号", orderNum = "6", width = 20)
    private String orderNo;
    @Excel(name = "订单来源", orderNum = "7", width = 15)
    private Integer source;
    @Excel(name = "大区", orderNum = "8", width = 15)
    private String region;
    @Excel(name = "所属业务中心", orderNum = "9", width = 20)
    private String inputStoreName;
    @Excel(name = "订单状态", orderNum = "10", width = 15)
    private String outterState;
    private Integer customerType;
    @Excel(name = "客户类型", orderNum = "11", width = 20)
    private String customerTypeStr;
    @Excel(name = "下单客户", orderNum = "12", width = 20)
    private String customerPhone;
    @Excel(name = "客户名称", orderNum = "13", width = 20)
    private String customerName;
    @Excel(name = "始发城市", orderNum = "14", width = 15)
    private String startCity;
    @Excel(name = "目的城市", orderNum = "15", width = 15)
    private String endCity;
    @Excel(name = "出发地业务中心", orderNum = "16", width = 20)
    private String startStoreName;
    @Excel(name = "目的地业务中心", orderNum = "17", width = 20)
    private String endStoreName;
    @Excel(name = "提车日期", orderNum = "18", width = 25)
    private Long expectStartDate;
    @Excel(name = "提车方式", orderNum = "19", width = 15)
    private Integer pickType;
    @Excel(name = "提车联系人", orderNum = "20", width = 20)
    private String pickContactName;
    @Excel(name = "提车联系方式", orderNum = "21", width = 20)
    private String pickContactPhone;
    @Excel(name = "提车地址", orderNum = "22", width = 20)
    private String startFullAddress;
    @Excel(name = "预计到达时间", orderNum = "23", width = 20)
    private Long expectEndDate;
    @Excel(name = "送车方式", orderNum = "24", width = 15)
    private Integer backType;
    @Excel(name = "交车联系人", orderNum = "25", width = 20)
    private String backContactName;
    @Excel(name = "交车电话", orderNum = "26", width = 20)
    private String backContactPhone;
    @Excel(name = "交车地址", orderNum = "27", width = 20)
    private String endFullAddress;
    @Excel(name = "订单备注", orderNum = "28", width = 15)
    private String remark;
    @Excel(name = "客户付款方式", orderNum = "29", width = 15)
    private Integer payType;
    @Excel(name = "是否能动", orderNum = "30", width = 15)
    private Integer isMove;
    @Excel(name = "车牌号", orderNum = "31", width = 15)
    private String plateNo;
    @Excel(name = "车值(万元)", orderNum = "32", width = 15, type = 10)
    private Integer valuation;
    @Excel(name = "追保额(万元)", orderNum = "33", width = 15, type = 10)
    private Integer addInsuranceAmount;
    @Excel(name = "保费(元)", orderNum = "34", width = 15, type = 10)
    private BigDecimal addInsuranceFee;
    @Excel(name = "提车费(元)", orderNum = "35", width = 15, type = 10)
    private BigDecimal pickFee;
    @Excel(name = "物流费(元)", orderNum = "36", width = 15, type = 10)
    private BigDecimal trunkFee;
    @Excel(name = "送车费(元)", orderNum = "37", width = 15, type = 10)
    private BigDecimal backFee;
    @Excel(name = "是否新车", orderNum = "38", width = 15)
    private Integer isNew;
    @Excel(name = "下单时间", orderNum = "39", width = 25)
    private Long createTime;
    @Excel(name = "下单人", orderNum = "40", width = 20)
    private String createUserName;
    @Excel(name = "接单时间", orderNum = "41", width = 25)
    private Long checkTime;
    @Excel(name = "接单人", orderNum = "42", width = 20)
    private String checkUserName;


    public String getWlPayState() {
        if (wlPayState == null) {
            return "";
        }
        String str = null;
        switch (wlPayState) {
            case 0:
                str = "未支付";
                break;
            case 2:
                str = "已支付";
                break;
            default:
                str = "";
        }
        return str;
    }

    public String getTotalFee() {
        return MoneyUtil.fenToYuan(totalFee, MoneyUtil.PATTERN_TWO);
    }

    public String getAddInsuranceFee() {
        return MoneyUtil.fenToYuan(addInsuranceFee, MoneyUtil.PATTERN_TWO);
    }

    public String getPickFee() {
        return MoneyUtil.fenToYuan(pickFee, MoneyUtil.PATTERN_TWO);
    }

    public String getTrunkFee() {
        return MoneyUtil.fenToYuan(trunkFee, MoneyUtil.PATTERN_TWO);
    }

    public String getBackFee() {
        return MoneyUtil.fenToYuan(backFee, MoneyUtil.PATTERN_TWO);
    }

    public String getSource() {
        if (source == null) {
            return "";
        }
        String str = null;
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
            default:
                str = "";
        }
        return str;
    }

    public String getExpectStartDate() {
        if (expectStartDate != null) {
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(expectStartDate), TimePatternConstant.DATETIME);
        }
        return "";
    }

    public String getExpectEndDate() {
        if (expectEndDate != null) {
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(expectEndDate), TimePatternConstant.DATETIME);
        }
        return "";
    }

    public String getCreateTime() {
        if (createTime != null) {
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(createTime), TimePatternConstant.DATETIME);
        }
        return "";
    }

    public String getCheckTime() {
        if (checkTime != null) {
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(checkTime), TimePatternConstant.DATETIME);
        }
        return "";
    }

    public String getPickType() {
        if (pickType == null) {
            return "";
        }
        String str = null;
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
            default:
                str = "";
        }
        return str;
    }

    public String getBackType() {
        if (backType == null) {
            return "";
        }
        String str = null;
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
            default:
                str = "";
        }
        return str;
    }

    public String getPayType() {
        if (payType == null) {
            return "";
        }
        String str = null;
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
            default:
                str = "";
        }
        return str;
    }

    public String getIsMove() {
        if (isMove == null) {
            return "";
        }
        String str = null;
        switch (isMove) {
            case 0:
                str = "否";
                break;
            case 1:
                str = "是";
                break;
            default:
                str = "";

        }
        return str;
    }

    public String getIsNew() {
        if (isNew == null) {
            return "";
        }
        String str = null;
        switch (isNew) {
            case 0:
                str = "否";
                break;
            case 1:
                str = "是";
                break;
            default:
                str = "";
        }
        return str;
    }

    /**
     * 客户类型code到name的转换
     *
     * @return
     */
    public String getCustomerTypeStr() {
        if (customerType == null) {
            return "";
        }
        switch (customerType) {
            case 1:
                return "C端";
            case 2:
                return "大客户";
            case 3:
                return "合伙人";
            default:
                return "";
        }
    }
}