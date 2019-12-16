package com.cjyc.common.model.vo.web.carrier;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CarrierExportExcel implements Serializable {
    private static final long serialVersionUID = -5902577956232138694L;
    @Excel(name = "企业名称" ,orderNum = "0",width = 25)
    private String name;
    @Excel(name = "公司联系人" ,orderNum = "1",width = 25)
    private String linkman;
    @Excel(name = "联系人手机号" ,orderNum = "2",width = 25)
    private String linkmanPhone;
    @Excel(name = "法人姓名" ,orderNum = "3",width = 25)
    private String legalName;
    @Excel(name = "法人身份证号" ,orderNum = "4",width = 25)
    private String legalIdCard;
    @Excel(name = "结算类型" ,orderNum = "5",width = 25)
    private Integer settleType;
    @Excel(name = "账期时间" ,orderNum = "6",width = 25)
    private Integer settlePeriod;
    @Excel(name = "承运方式" ,orderNum = "7",width = 25)
    private Integer mode;
    @Excel(name = "是否开发票" ,orderNum = "8",width = 25)
    private Integer isInvoice;
    @Excel(name = "卡类型" ,orderNum = "9",width = 25)
    private Integer cardType;
    @Excel(name = "持卡人名称" ,orderNum = "10",width = 25)
    private String cardName;
    @Excel(name = "开户银行" ,orderNum = "11",width = 25)
    private String bankName;
    @Excel(name = "银行卡号" ,orderNum = "12",width = 25)
    private String cardNo;
    @Excel(name = "营业执照正面" ,orderNum = "13",width = 25)
    private String busLicenseFrontImg;
    @Excel(name = "营业执照反面" ,orderNum = "14",width = 25)
    private String busLicenseBackImg;
    @Excel(name = "道路运输许可证正面照片" ,orderNum = "15",width = 25)
    private String transportLicenseFrontImg;
    @Excel(name = "道路运输许可证反面照片" ,orderNum = "16",width = 25)
    private String transportLicenseBackImg;
    @Excel(name = "银行开户证明正面" ,orderNum = "17",width = 25)
    private String bankOpenFrontImg;
    @Excel(name = "银行开户证明反面" ,orderNum = "18",width = 25)
    private String bankOpenBackImg;
    @Excel(name = "总运输台数" ,orderNum = "19",width = 25)
    private Integer carNum;
    @Excel(name = "总收入" ,orderNum = "20",width = 25)
    private BigDecimal totalIncome;
    @Excel(name = "状态" ,orderNum = "21",width = 25)
    private Integer state;
    @Excel(name = "最后操作时间" ,orderNum = "22",width = 25)
    private Long operateTime;
    @Excel(name = "最后操作人" ,orderNum = "23",width = 25)
    private String operateName;
    public String getIsInvoice(){
        if(isInvoice != null){
            if(isInvoice == 0){
                return "否";
            }else if(isInvoice == 1){
                return "是";
            }
        }
        return "";
    }
    public String getCardType(){
        if(cardType != null){
            if(cardType == 1){
                return "公户";
            }else if(cardType == 2){
                return "私户";
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
    public String getOperateTime(){
        if(operateTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(operateTime), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }
}