package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;

public interface ICsTaskService {
    String getTaskNo(String waybillNo);

    ResultVo allot(AllotTaskDto paramsDto);

    ResultVo<ResultReasonVo> load(LoadTaskDto paramsDto);

    ResultVo<ResultReasonVo> unload(UnLoadTaskDto paramsDto);

    ResultVo<ResultReasonVo> outStore(OutStoreTaskDto paramsDto);

    ResultVo<ResultReasonVo> inStore(InStoreTaskDto paramsDto);

    ResultVo receipt(ReceiptTaskDto reqDto);

}
