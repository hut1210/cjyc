package com.cjyc.common.model.vo.salesman.mine;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class AppContractVo implements Serializable {
    private static final long serialVersionUID = -7212143034994046302L;

    private List<Map<String,Object>> list;
}