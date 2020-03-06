package com.cjyc.common.model.dto.web.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Data
public class ChangePriceExportDto {
    private String startDateStr;
    private String endDateStr;

    @ApiModelProperty(hidden = true)
    private Long endDate;
    @ApiModelProperty(hidden = true)
    private Long startDate;

    public Long getEndDate() {
        if(StringUtils.isBlank(getEndDateStr())){
            return null;
        }
        String pt;
        if(getEndDateStr().contains("/")){
            pt = "yyyy/MM/dd";
        }else if(getEndDateStr().contains("-")){
            pt = "yyyy-MM-dd";
        }else{
            pt = "yyyy.MM.dd";
        }
        LocalDate localDate = LocalDate.parse(getEndDateStr(), DateTimeFormatter.ofPattern(pt));
        return localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
    }

    public Long getStartDate() {
        if(StringUtils.isBlank(getStartDateStr())){
            return null;
        }
        String pt;
        if(getEndDateStr().contains("/")){
            pt = "yyyy/MM/dd";
        }else if(getEndDateStr().contains("-")){
            pt = "yyyy-MM-dd";
        }else{
            pt = "yyyy.MM.dd";
        }
        LocalDate localDate = LocalDate.parse(getEndDateStr(), DateTimeFormatter.ofPattern(pt));
        return localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
    }
}
