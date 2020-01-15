package com.cjyc.common.model.vo.customer.customerLine;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class CustomerLineVo implements Serializable {
    private static final long serialVersionUID = 2120381343345734965L;

    @ApiModelProperty("线路id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("起始地址")
    private String startAdress;

    @ApiModelProperty("起始地联系人")
    private String startContact;

    @ApiModelProperty("起始地联系人电话")
    private String startContactPhone;

    @ApiModelProperty("目的地地址")
    private String endAdress;

    @ApiModelProperty("目的地联系人")
    private String endContact;

    @ApiModelProperty("目的地联系人电话")
    private String endContactPhone;

    public String getStartAdress(){return StringUtils.isBlank(startAdress) ? "":startAdress; }
    public String getStartContact(){return StringUtils.isBlank(startContact) ? "":startContact; }
    public String getStartContactPhone(){return StringUtils.isBlank(startContactPhone) ? "":startContactPhone; }
    public String getEndAdress(){return StringUtils.isBlank(endAdress) ? "":endAdress; }
    public String getEndContact(){return StringUtils.isBlank(endContact) ? "":endContact; }
    public String getEndContactPhone(){return StringUtils.isBlank(endContactPhone) ? "":endContactPhone; }
}