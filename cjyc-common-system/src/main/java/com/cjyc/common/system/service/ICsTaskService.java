package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.customer.order.ReceiptBatchDto;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;

import java.util.List;

public interface ICsTaskService {
    String getTaskNo(String waybillNo);

    ResultVo allot(AllotTaskDto paramsDto);

    ResultVo<ResultReasonVo> load(LoadTaskDto paramsDto);

    ResultVo<ResultReasonVo> unload(UnLoadTaskDto paramsDto);

    ResultVo<ResultReasonVo> outStore(OutStoreTaskDto paramsDto);

    ResultVo<ResultReasonVo> inStore(InStoreTaskDto paramsDto);

    ResultVo receipt(ReceiptTaskDto reqDto);

    ResultVo<ResultReasonVo> receiptBatch(ReceiptBatchDto reqDto);

    /**
     * 车辆完成更新订单、运单、任务状态
     * @author JPG
     * @date 2019/12/8 12:06
     * @param orderCarNoList 车辆编号列表
     * @param userInfo
     * @return
     */
    void updateForCarFinish(List<String> orderCarNoList, UserInfo userInfo);
}
