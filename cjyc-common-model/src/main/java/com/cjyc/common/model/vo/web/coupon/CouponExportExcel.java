package com.cjyc.common.model.vo.web.coupon;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CouponExportExcel implements Serializable {
    private static final long serialVersionUID = -103317215675376981L;
    @Excel(name = "优惠券名称" ,orderNum = "0",width = 25)
    private String name;
    @Excel(name = "优惠券类型" ,orderNum = "1",width = 25)
    private Integer type;
    @Excel(name = "满额价" ,orderNum = "2",width = 25)
    private BigDecimal fullAmount;
    @Excel(name = "减额值" ,orderNum = "3",width = 25)
    private BigDecimal cutAmount;
    @Excel(name = "折扣" ,orderNum = "4",width = 25)
    private BigDecimal discount;
    @Excel(name = "发放张数" ,orderNum = "5",width = 25)
    private Integer grantNum;
    @Excel(name = "领取张数" ,orderNum = "6",width = 25)
    private Integer receiveNum;
    @Excel(name = "消耗张数" ,orderNum = "7",width = 25)
    private Integer consumeNum;
    @Excel(name = "到期作废张数" ,orderNum = "8",width = 25)
    private Integer expireDeleNum;
    @Excel(name = "剩余可用张数" ,orderNum = "9",width = 25)
    private Integer surplusAvailNum;
    @Excel(name = "是否永久" ,orderNum = "10",width = 25)
    private Integer isForever;
    @Excel(name = "优惠券有效起始时间" ,orderNum = "11",width = 25)
    private Long startPeriodDate;
    @Excel(name = "优惠券有效结束时间" ,orderNum = "12",width = 25)
    private Long endPeriodDate;
    @Excel(name = "状态" ,orderNum = "13",width = 25)
    private Integer state;
    @Excel(name = "创建时间" ,orderNum = "14",width = 25)
    private Long createTime;
    @Excel(name = "创建人" ,orderNum = "15",width = 25)
    private String createName;
    public String getType(){
        if(type != null){
            if(type == 0){
                return "满减 ";
            }else if(type == 1){
                return "直减";
            }else if(type == 2){
                return "折扣";
            }
        }
        return "";
    }
    public BigDecimal getFullAmount(){
        return fullAmount == null ? BigDecimal.ZERO:fullAmount.divide(new BigDecimal(100));
    }
    public BigDecimal getCutAmount(){
        return cutAmount == null ? BigDecimal.ZERO:cutAmount.divide(new BigDecimal(100));
    }
    public String getIsForever(){
        if(isForever != null){
            if(isForever == 0){
                return "否";
            }else if(isForever == 1){
                return "是";
            }
        }
        return "";
    }
    public String getStartPeriodDate(){
        if(startPeriodDate != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(startPeriodDate), TimePatternConstant.DATE);
        }
        return "";
    }
    public String getEndPeriodDate(){
        if(endPeriodDate != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(endPeriodDate), TimePatternConstant.DATE);
        }
        return "";
    }
    public String getCreateTime(){
        if(createTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(createTime), TimePatternConstant.COMPLEX_TIME_FORMAT);
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
    public Integer getGrantNum(){return grantNum == null ? 0:grantNum;}
    public Integer getReceiveNum(){return receiveNum == null ? 0:receiveNum;}
    public Integer getConsumeNum(){return consumeNum == null ? 0:consumeNum;}
    public Integer getExpireDeleNum(){return expireDeleNum == null ? 0:expireDeleNum;}
    public Integer getSurplusAvailNum(){return surplusAvailNum == null ? 0:surplusAvailNum;}
}