package com.cjyc.common.model.vo.web.role;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class ExportRoleVo implements Serializable {
    private static final long serialVersionUID = 6421765448649646688L;

    @Excel(name = "角色名称" ,orderNum = "0",width = 15)
    private String roleName;
    @Excel(name = "角色机构级别" ,orderNum = "1",width = 15)
    private Integer roleLevel;
    @Excel(name = "角色范围" ,orderNum = "2",width = 15)
    private Integer roleRange;

    public String getRoleLevel(){
        if(roleLevel != null){
            if(roleLevel == 1){
                return "全国";
            }else if(roleLevel == 2){
                return "大区";
            }else if(roleLevel == 3){
                return "省";
            }else if(roleLevel == 4){
                return "城市";
            }else if(roleLevel == 5){
                return "业务中心";
            }
        }
        return "";
    }
    public String getRoleRange(){
        if(roleRange != null){
            if(roleRange == 1){
                return "韵车内部";
            }else if(roleRange == 2){
                return "承运商";
            }else if(roleRange == 3){
                return "客户";
            }
        }
        return "";
    }
}