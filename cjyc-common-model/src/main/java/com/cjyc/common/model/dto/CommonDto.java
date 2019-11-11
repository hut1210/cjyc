package com.cjyc.common.model.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class CommonDto extends BasePageDto implements Serializable {


    private Long loginId;
}