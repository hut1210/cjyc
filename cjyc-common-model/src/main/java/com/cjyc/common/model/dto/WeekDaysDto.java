package com.cjyc.common.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Package: com.cjyc.common.model.dto
 * @Description:
 * @Author: Yang.yanfei
 * @Date: 2020/4/16
 * @Version: V1.0
 * @Copyright: 2019 - 2020 - ©长久科技
 */
@Data
public class WeekDaysDto implements Serializable {
    private String date;
    private int type;
}
