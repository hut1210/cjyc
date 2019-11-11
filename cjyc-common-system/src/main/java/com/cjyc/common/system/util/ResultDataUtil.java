package com.cjyc.common.system.util;

import com.cjkj.common.model.ResultData;

public class ResultDataUtil {

    public static boolean isEmpty(ResultData resultData){
        return resultData == null || resultData.getData() == null;
    }
}
