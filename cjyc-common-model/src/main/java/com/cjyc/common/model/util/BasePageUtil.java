package com.cjyc.common.model.util;

import com.cjyc.common.model.dto.BasePageDto;

import java.io.Serializable;

public class BasePageUtil implements Serializable {

    public static void initPage(BasePageDto basePageDto){
        if(basePageDto.getCurrentPage() == null || basePageDto.getCurrentPage() < 1){
            basePageDto.setCurrentPage(1);
        }
        if(basePageDto.getPageSize() == null || basePageDto.getPageSize() < 1){
            basePageDto.setPageSize(10);
        }
    }
}