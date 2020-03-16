package com.cjyc.common.model.dto.web.excel;

import com.cjyc.common.model.constant.TimeConstant;
import com.cjyc.common.model.util.DateUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;
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

    public Long getEndDate() {
        String endDateStr = getEndDateStr();
        if(StringUtils.isBlank(endDateStr)){
            return null;
        }
        return DateUtil.parseDate(endDateStr).getTime();
    }

    public Long getStartDate() {
        String startDateStr = getStartDateStr();
        if(StringUtils.isBlank(startDateStr)){
            return null;
        }
        return DateUtil.parseDate(startDateStr).getTime() + TimeConstant.MILLS_OF_ONE_DAY;
    }
}
