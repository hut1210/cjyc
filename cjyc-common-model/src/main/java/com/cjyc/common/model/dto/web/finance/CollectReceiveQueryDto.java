package com.cjyc.common.model.dto.web.finance;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hut
 * @Date: 2019/12/16 15:17
 */
@Data
public class CollectReceiveQueryDto extends BasePageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "运单单号")
    private String wayBillNo;

    @ApiModelProperty(value = "运单类型")
    private Integer wayBillType;

    @ApiModelProperty(value = "代收款人")
    private String collectMan;

    @ApiModelProperty(value = "代收款人电话")
    private String collectManPhone;

    @ApiModelProperty(value = "回款状态")
    private String returnStatus;

}
