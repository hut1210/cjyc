package com.cjyc.common.model.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Description 金额返回数据处理
 * @Author LiuXingXiang
 * @Date 2019/11/12 16:02
 **/
public class BigDecimalSerizlizer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal amount, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 将金额分 转换为元 如果没有小数位，则加上小数位
        if(!Objects.isNull(amount)){
            String str = String.valueOf(amount.divide(new BigDecimal(100)));
            if (!str.contains(".")) {
                str = str + ".00";
            }
            jsonGenerator.writeString(str);
        }else{
            jsonGenerator.writeString("0.00");
        }
    }
}
