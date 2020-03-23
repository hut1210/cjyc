package com.cjyc.common.model.serizlizer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @Description 金额返回数据处理
 * @Author LiuXingXiang
 * @Date 2019/11/12 16:02
 **/
public class BigDecimalSerizlizer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal amount, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 将分转换为元，结果保留两位小数
        if(!Objects.isNull(amount)){
            DecimalFormat df =new DecimalFormat("0.00");
            BigDecimal value = amount.divide(BigDecimal.valueOf(100));
            String str = amount.equals(BigDecimal.ZERO) ? "0.00" : df.format(value);
            jsonGenerator.writeString(str);
        } else {
            jsonGenerator.writeString("0.00");
        }
    }
}
