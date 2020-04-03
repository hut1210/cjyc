package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.customer.order.ReceiptBatchDto;
import com.cjyc.common.model.dto.driver.task.PickLoadDto;
import com.cjyc.common.model.dto.driver.task.ReplenishInfoDto;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.entity.defined.CarrierInfo;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;

import java.util.List;

public interface ICsTaskService {
    /**
     * 获取任务编号
     * @author JPG
     * @since 2019/12/9 18:10
     * @param waybillNo
     */
    String getTaskNo(String waybillNo);

    /**
     * 完善提车信息
     * @author JPG
     * @since 2019/12/9 18:10
     * @param reqDto
     */
    ResultVo replenishInfo(ReplenishInfoDto reqDto);

    ResultVo allot(AllotTaskDto paramsDto);

    ResultVo<ResultReasonVo> load(BaseTaskDto paramsDto);

    ResultVo<ResultReasonVo> unload(BaseTaskDto paramsDto);

    ResultVo<ResultReasonVo> outStore(BaseTaskDto paramsDto);

    ResultVo<ResultReasonVo> inStore(BaseTaskDto paramsDto);

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

    ResultVo<ResultReasonVo>  loadForLocal(ReplenishInfoDto paramsDto);

    void reCreate(Waybill waybill, List<WaybillCar> waybillCarList, List<WaybillCar> newWaybillCar,CarrierInfo carrierInfo);

    void updateForTaskCarFinish(List<String> taskCarIdList, int payType, UserInfo userInfo);

    ResultVo inStoreForLocal(ReplenishInfoDto reqDto);

    int validateAndFinishTaskWaybill(Task task);
    int validateAndFinishTask(Task task);
}
