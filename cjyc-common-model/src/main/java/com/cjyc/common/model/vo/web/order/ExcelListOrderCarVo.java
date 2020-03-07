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

    @Excel(name = "车辆编码", orderNum = "0")
    private String no;
    @Excel(name = "vin码", orderNum = "1")
    private String vin;
    @Excel(name = "提车运输状态 1待调度，2待提车，3待交车，5已完成，21自送待调度，23自送待交车，25自送已交付，100物流上门", orderNum = "2")
    private Integer pickTransportState;
    @Excel(name = "干线运输状态 1待调度，2待提车，3待交车，5已完成, 100无干线", orderNum = "3")
    private Integer trunkTransportState;
    @Excel(name = "送车运输状态 1待调度，2待提车，3待交车，5已完成，21自提待调度，23自提待交车，25自提已交付，100物流上门", orderNum = "4")
    private Integer backTransportState;
    @Excel(name = "应收状态：0未支付，2已支付", orderNum = "5")
    private Integer wlPayState;
    @Excel(name = "品牌", orderNum = "6")
    private String brand;
    @Excel(name = "车系", orderNum = "7")
    private String model;
    @Excel(name = "实收总运费(元)", orderNum = "8")
    private BigDecimal totalFee;
    @Excel(name = "订单编号", orderNum = "9")
    private String orderNo;
    @Excel(name = "订单来源：1WEB管理后台, 2业务员APP, 4司机APP, 6用户端APP, 7用户端小程序", orderNum = "10")
    private Integer source;
    @Excel(name = "大区", orderNum = "11")
    private String region;
    @Excel(name = "订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）", orderNum = "12")
    private String outterState;
    @Excel(name = "下单客户", orderNum = "13")
    private String customerPhone;
    @Excel(name = "客户名称", orderNum = "14")
    private String customerName;
    @Excel(name = "始发城市", orderNum = "15")
    private String startCity;
    @Excel(name = "目的城市", orderNum = "16")
    private String endCity;
    @Excel(name = "出发地业务中心", orderNum = "17")
    private String startStoreName;
    @Excel(name = "目的地业务中心名称", orderNum = "18")
    private String endStoreName;
    @Excel(name = "预计出发时间（提车日期）", orderNum = "19")
    private Long expectStartDate;
    @Excel(name = "提车方式:1 自送，2代驾上门，3拖车上门，4物流上门", orderNum = "20")
    private Integer pickType;
    @Excel(name = "提车联系人", orderNum = "21")
    private String pickContactName;
    @Excel(name = "提车联系方式", orderNum = "22")
    private String pickContactPhone;
    @Excel(name = "提车地址", orderNum = "23")
    private String startFullAddress;
    @Excel(name = "预计到达时间", orderNum = "24")
    private Long expectEndDate;
    @Excel(name = "送车方式： 1 自提，2代驾上门，3拖车上门，4物流上门", orderNum = "25")
    private Integer backType;
    @Excel(name = "交车联系人", orderNum = "26")
    private String backContactName;
    @Excel(name = "交车电话", orderNum = "27")
    private String backContactPhone;
    @Excel(name = "交车地址", orderNum = "28")
    private String endFullAddress;
    @Excel(name = "订单备注", orderNum = "29")
    private String remark;
    @Excel(name = "客户付款方式：0到付（默认），1预付，2账期", orderNum = "30")
    private Integer payType;
    @Excel(name = "是否能动 0-否 1-是", orderNum = "31")
    private Integer isMove;
    @Excel(name = "车牌号", orderNum = "32")
    private String plateNo;
    @Excel(name = "车值(万元)", orderNum = "33")
    private Integer valuation;
    @Excel(name = "追保额(万元)", orderNum = "34")
    private Integer addInsuranceAmount;
    @Excel(name = "保费(元)", orderNum = "35")
    private BigDecimal addInsuranceFee;
    @Excel(name = "提车费(元)", orderNum = "36")
    private BigDecimal pickFee;
    @Excel(name = "物流费(元)", orderNum = "37")
    private BigDecimal trunkFee;
    @Excel(name = "送车费(元)", orderNum = "38")
    private BigDecimal backFee;
    @Excel(name = "是否新车 0-否 1-是", orderNum = "39")
    private Integer isNew;
    @Excel(name = "下单时间", orderNum = "40")
    private Long createTime;
    @Excel(name = "下单人", orderNum = "41")
    private String createUserName;
    @Excel(name = "接单时间", orderNum = "42")
    private Long checkTime;
    @Excel(name = "接单人", orderNum = "43")
    private String checkUserName;

    public String getPickTransportState(){
        if(pickTransportState == null){
            if(pickTransportState == 1){
                return "待调度";
            }else if(pickTransportState == 2){
                return "待提车";
            }else if(pickTransportState == 3){
                return "待交车";
            }else if(pickTransportState == 5){
                return "已完成";
            }else if(pickTransportState == 21){
                return "自送待调度";
            }else if(pickTransportState == 23){
                return "自送待交车";
            }else if(pickTransportState == 25){
                return "自送已交付";
            }else if(pickTransportState == 100){
                return "物流上门";
            }
        }
        return "";
    }
    public String getTrunkTransportState(){
        if(trunkTransportState == null){
            if(trunkTransportState == 1){
                return "待调度";
            }else if(trunkTransportState == 2){
                return "待提车";
            }else if(trunkTransportState == 3){
                return "待交车";
            }else if(trunkTransportState == 5){
                return "已完成";
            }else if(trunkTransportState == 100){
                return "无干线";
            }
        }
        return "";
    }
    public String getBackTransportState(){
        if(backTransportState == null){
            if(backTransportState == 1){
                return "待调度";
            }else if(backTransportState == 2){
                return "待提车";
            }else if(backTransportState == 3){
                return "待交车";
            }else if(backTransportState == 5){
                return "已完成";
            }else if(backTransportState == 21){
                return "自提待调度";
            }else if(backTransportState == 23){
                return "自提待交车";
            }else if(backTransportState == 25){
                return "自提已交付";
            }else if(backTransportState == 100){
                return "物流上门";
            }
        }
        return "";
    }

    public String getWlPayState(){
        if(wlPayState != null){
            if(wlPayState == 0){
                return "未支付";
            }else if(wlPayState == 1){
                return "已支付";
            }
        }
        return "";
    }
    public String getTotalFee() { return MoneyUtil.fenToYuan(totalFee, MoneyUtil.PATTERN_TWO); }
    public String getAddInsuranceFee() { return MoneyUtil.fenToYuan(addInsuranceFee, MoneyUtil.PATTERN_TWO); }
    public String getPickFee() { return MoneyUtil.fenToYuan(pickFee, MoneyUtil.PATTERN_TWO); }
    public String getTrunkFee() { return MoneyUtil.fenToYuan(trunkFee, MoneyUtil.PATTERN_TWO); }
    public String getBackFee() { return MoneyUtil.fenToYuan(backFee, MoneyUtil.PATTERN_TWO); }
    public String getSource(){
        if(source == null){
            if(source == 1){
                return "WEB管理后台";
            }else if(source == 2){
                return "业务员APP";
            }else if(source == 4){
                return "司机APP";
            }else if(source == 6){
                return "用户端APP";
            }else if(source == 7){
                return "用户端小程序";
            }
        }
        return "";
    }
    public String getExpectStartDate(){
        if(expectStartDate != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(expectStartDate), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }
    public String getExpectEndDate(){
        if(expectEndDate != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(expectEndDate), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }
    public String getCreateTime(){
        if(createTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(createTime), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }
    public String getCheckTime(){
        if(checkTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(checkTime), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }
    public String getPickType(){
        if(pickType == null){
            if(pickType == 1){
                return "自送";
            }else if(pickType == 2){
                return "代驾上门";
            }else if(pickType == 3){
                return "拖车上门";
            }else if(pickType == 4){
                return "物流上门";
            }
        }
        return "";
    }
    public String getBackType(){
        if(backType == null){
            if(backType == 1){
                return "自提";
            }else if(backType == 2){
                return "代驾上门";
            }else if(backType == 3){
                return "拖车上门";
            }else if(backType == 4){
                return "物流上门";
            }
        }
        return "";
    }
    public String getPayType(){
        if(payType == null){
            if(payType == 0){
                return "到付";
            }else if(payType == 1){
                return "预付";
            }else if(payType == 2){
                return "账期";
            }
        }
        return "";
    }
    public String getIsMove(){
        if(isMove == null){
            if(isMove == 0){
                return "否";
            }else if(isMove == 1){
                return "是";
            }
        }
        return "";
    }
    public String getIsNew(){
        if(isNew == null){
            if(isNew == 0){
                return "否";
            }else if(isNew == 1){
                return "是";
            }
        }
        return "";
    }




}