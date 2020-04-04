package com.cjyc.common.model.serizlizer;

import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Objects;

public class SecondLongSerizlizer extends JsonSerializer<Long> {
    @Override
    public void serialize(Long date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        //将毫秒值转换成秒变成char型数据返回
        if(!Objects.isNull(date) && date != 0){
            String dateStr = LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.DATETIME);
            jsonGenerator.writeString(dateStr);
        } else {
            jsonGenerator.writeString("");
        }
    }
}