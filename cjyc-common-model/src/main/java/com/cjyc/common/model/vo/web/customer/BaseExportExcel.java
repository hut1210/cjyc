package com.cjyc.common.model.vo.web.customer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BaseExportExcel implements Serializable {
    private static final long serialVersionUID = 6098047110054222064L;
    @Excel(name = "客户编码" ,orderNum = "0",width = 15)
    private String customerNo;

    @Excel(name = "手机号" ,orderNum = "1",width = 15)
    private String contactPhone;

    @Excel(name = "联系人/姓名" ,orderNum = "2",width = 15)
    private String contactMan;

    @Excel(name = "账号来源" ,orderNum = "3",width = 15)
    private Integer source;

    @Excel(name = "注册时间" ,orderNum = "4",width = 25)
    private Long createTime;

    @Excel(name = "注册操作人" ,orderNum = "5",width = 15)
    private String createUserName;

    @Excel(name = "总单量" ,orderNum = "6",width = 15)
    private Integer totalOrder;

    @Excel(name = "总运车量" ,orderNum = "7",width = 15)
    private Integer totalCar;

    @Excel(name = "订单总金额" ,orderNum = "8",width = 15)
    private BigDecimal totalAmount;

    public String getSource(){
        if(source != null){
            if(source == 1){
                return "App注册";
            }else if(source == 2){
                return "Applet注册";
            }else if(source == 3){
                return "韵车后台";
            }else if(source == 4){
                return "升级创建";
            }
        }
        return "";
    }
    public String getCreateTime(){
        if(createTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(createTime), TimePatternConstant.DATETIME);
        }
        return "";
    }
}