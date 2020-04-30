package com.cjyc.common.model.entity.defined;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MQMessage<T> {

    private String type;
    private T data;

}
