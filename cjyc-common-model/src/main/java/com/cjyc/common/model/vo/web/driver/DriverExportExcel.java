package com.cjyc.common.model.vo.web.driver;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DriverExportExcel implements Serializable {
    private static final long serialVersionUID = 5098189971333492778L;
    @Excel(name = "司机姓名" ,orderNum = "0",width = 25)
    private String realName;
    @Excel(name = "司机手机号" ,orderNum = "1",width = 25)
    private String phone;
    @Excel(name = "承运方式" ,orderNum = "2",width = 25)
    private Integer mode;
    @Excel(name = "结算类型" ,orderNum = "3",width = 25)
    private Integer settleType;
    @Excel(name = "车牌号" ,orderNum = "4",width = 25)
    private String plateNo;
    @Excel(name = "持卡人" ,orderNum = "5",width = 25)
    private String cardName;
    @Excel(name = "开户行" ,orderNum = "6",width = 25)
    private String bankName;
    @Excel(name = "卡号" ,orderNum = "7",width = 25)
    private String cardNo;
    @Excel(name = "身份证号" ,orderNum = "8",width = 25)
    private String idCard;
    @Excel(name = "身份证正面" ,orderNum = "9",width = 25)
    private String idCardFrontImg;
    @Excel(name = "身份证反面" ,orderNum = "10",width = 25)
    private String idCardBackImg;
    @Excel(name = "驾驶证正面" ,orderNum = "11",width = 25)
    private String driverLicenceFrontImg;
    @Excel(name = "驾驶证反面" ,orderNum = "12",width = 25)
    private String driverLicenceBackImg;
    @Excel(name = "行驶证正面" ,orderNum = "13",width = 25)
    private String travelLicenceFrontImg;
    @Excel(name = "从业证正面" ,orderNum = "14",width = 25)
    private String qualifiCertFrontImg;
    @Excel(name = "营运证正面" ,orderNum = "15",width = 25)
    private String taxiLicenceFrontImg;
    @Excel(name = "账号来源" ,orderNum = "16",width = 25)
    private Integer source;
    @Excel(name = "运行状态" ,orderNum = "17",width = 25)
    private Integer businessState;
    @Excel(name = "总运量(台)" ,orderNum = "18",width = 25, type = 10)
    private Integer carNum;
    @Excel(name = "总收入" ,orderNum = "19",width = 25, type = 10)
    private BigDecimal totalIncome;
    @Excel(name = "状态" ,orderNum = "20",width = 25)
    private Integer state;
    @Excel(name = "最后操作时间" ,orderNum = "21",width = 25)
    private Long operatTime;
    @Excel(name = "操作人" ,orderNum = "22",width = 25)
    private String operator;
    public String getMode(){
        if(mode != null){
            if(mode == 2){
                return "代驾";
            }else if(mode == 3){
                return "干线";
            }else if(mode == 4){
                return "拖车";
            }else if(mode == 5){
                return "代驾+干线";
            }else if(mode == 6){
                return "代驾+拖车";
            }else if(mode == 7){
                return "干线+拖车";
            }else if(mode == 9){
                return "代驾+干线+拖车";
            }
        }
        return "";
    }
    public String getSettleType(){
        if(settleType != null){
            if(settleType == 0){
                return "时付";
            }else if(settleType == 1){
                return "账期";
            }
        }
        return "";
    }
    public String getBusinessState(){
        if(businessState != null){
            if(businessState == 0){
                return "空闲";
            }else if(businessState == 1){
                return "繁忙";
            }
        }
        return "";
    }
    public String getState(){
        if(state != null){
            if(state == 0){
                return "待审核";
            }else if(state == 1){
                return "审核中";
            }else if(state == 2){
                return "审核通过";
            }else if(state == 5){
                return "冻结";
            }else if(state == 7){
                return "审核拒绝";
            }else if(state == 9){
                return "已停用";
            }
        }
        return "";
    }
    public String getSource(){
        if(source != null){
            if(source == 1){
                return "App注册";
            }else if(source == 2){
                return "Applet注册";
            }else if(source == 3){
                return "业务员创建";
            }else if(source == 4){
                return "承运商管理员创建";
            }else if(source == 11){
                return "掌控接口";
            }else if(source == 12){
                return "otm接口";
            }
        }
        return "";
    }
    public String getOperatTime(){
        if(operatTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(operatTime), TimePatternConstant.DATETIME);
        }
        return "";
    }
}