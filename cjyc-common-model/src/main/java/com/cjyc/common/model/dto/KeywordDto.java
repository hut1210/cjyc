package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jboss.logging.Param;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
@Data
public class KeywordDto implements Serializable {

    private static final long serialVersionUID = -3079080818646393749L;

    @ApiModelProperty("关键字")
    private String keyword;
}