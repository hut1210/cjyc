package com.cjyc.common.model.dto.web.excel;

import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
public class ChangePriceExportDto {
    private String startDateStr;
    private String endDateStr;

    @ApiModelProperty(hidden = true)
    private Long endDate;
    @ApiModelProperty(hidden = true)
    private Long startDate;

    public String getEndDate() {
c
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
        DateTimeFormatter.ofPattern(pt).parse(getEndDateStr());
        return LocalDateTimeUtil.convertToLong(LocalDateTime.parse(getEndDateStr(),DateTimeFormatter.ofPattern(pt)));
    }

    public String getStartDate() {
        return "";
    }
}
