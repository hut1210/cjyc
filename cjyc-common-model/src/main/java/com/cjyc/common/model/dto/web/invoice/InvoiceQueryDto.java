package com.cjyc.common.model.dto.web.invoice;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 发票申请信息查询实体
 * @Author LiuXingXiang
 * @Date 2019/11/4 14:42
 **/
@Data
public class InvoiceQueryDto extends BasePageDto {
    private static final long serialVersionUID = -6634711928020419453L;
    @ApiModelProperty(value = "申请人名称")
    private String customerName;

    @ApiModelProperty(value = "申请时间开始")
    private Long applyTimeStart;

    @ApiModelProperty(value = "申请时间结束")
    private Long applyTimeEnd;

    @ApiModelProperty(value = "开票时间开始")
    private Long invoiceTimeStart;

    @ApiModelProperty(value = "开票时间结束")
    private Long invoiceTimeEnd;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "开票人姓名")
    private String operationName;

    @ApiModelProperty(value = "开票状态 1-申请中，2-已开票")
    private Integer state;
}
