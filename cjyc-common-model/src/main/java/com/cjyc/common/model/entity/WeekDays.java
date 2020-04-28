package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Package: com.cjyc.common.model.entity
 * @Description:
 * @Author: Yang.yanfei
 * @Date: 2020/4/15
 * @Version: V1.0
 * @Copyright: 2019 - 2020 - ©长久科技
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_weekdays")
@ApiModel(value="工作日和休息日对象", description="工作日和休息日")
public class WeekDays implements Serializable {
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "类型，0=工作日；1=休息日")
    private int type;
}
