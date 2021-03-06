package com.cjyc.salesman.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.salesman.BaseSalesDto;
import com.cjyc.common.model.dto.salesman.mine.AchieveDto;
import com.cjyc.common.model.dto.salesman.mine.OrderCarDto;
import com.cjyc.common.model.dto.salesman.mine.StockCarDto;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.mine.QRCodeVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarDetailVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarVo;

import java.util.Map;

public interface IMineService extends IService<WaybillCar> {

    /**
     * 库存车辆
     * @param dto
     * @return
     */
    ResultVo<PageVo<StockCarVo>> findStockCar(StockCarDto dto);

    /**
     * 库存车辆详情
     * @param dto
     * @return
     */
    ResultVo<StockCarDetailVo> findStockCarDetail(OrderCarDto dto);

    /**
     * 业绩统计
     * @param dto
     * @return
     */
    ResultVo<Map<String,Object>> achieveCount(AchieveDto dto);

    /**
     * 二维码
     * @param dto
     * @return
     */
    ResultVo<QRCodeVo> findQrCode(BaseSalesDto dto);
}
