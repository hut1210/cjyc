package com.cjyc.common.model.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by youyou on 2019/7/22.
 * @date 2019年7月22日08:44:15
 * 子系统、菜单及部门对应关系
 */
@Data
@TableName("sys_dept_menu")
public class SysDeptMenuEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sysId;

    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;
}
