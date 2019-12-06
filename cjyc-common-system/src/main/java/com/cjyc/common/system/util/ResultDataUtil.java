package com.cjyc.common.system.util;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;

public class ResultDataUtil {

    public static boolean isEmpty(ResultData resultData){
        return resultData == null || resultData.getData() == null;
    }

    /**
     * ResultData是否处理成功
     * @param resultData
     * @return
     */
    public static boolean isSuccess(ResultData resultData) {
        if (resultData == null) {
            return false;
        }
        if (ReturnMsg.SUCCESS.getCode().equals(resultData.getCode())) {
            return true;
        }
        return false;
    }
}
