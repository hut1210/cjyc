package com.cjyc.common.model.vo.web.admin;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
@Data
public class ExportAdminVo implements Serializable {
    private static final long serialVersionUID = -7331411911303433158L;
    @Excel(name = "状态" ,orderNum = "0",width = 15)
    private Integer state;
    @Excel(name = "用户名称" ,orderNum = "1",width = 20)
    private String name;
    @Excel(name = "账号" ,orderNum = "2",width = 15)
    private String account;
    @Excel(name = "角色列表描述" ,orderNum = "3",width = 20)
    private String roles;
    @Excel(name = "业务范围描述信息" ,orderNum = "4",width = 20)
    private String bizDesc;
    @Excel(name = "创建时间" ,orderNum = "5",width = 20)
    private Long createTime;
    @Excel(name = "创建人" ,orderNum = "6",width = 15)
    private String createUser;

    public String getState(){
        if(state != null){
            if(state == 0){
                return "待审核";
            }else if(state == 2){
                return "已启用";
            }else if(state == 4){
                return "取消";
            }else if(state == 7){
                return "已驳回";
            }else if(state == 9){
                return "已停用";
            }
        }
        return "";
    }
    public String getCreateTime(){
        if(createTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(createTime), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }
}