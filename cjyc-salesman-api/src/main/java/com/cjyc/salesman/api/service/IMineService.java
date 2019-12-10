package com.cjyc.salesman.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.salesman.BaseSalesDto;
import com.cjyc.common.model.dto.salesman.mine.AchieveDto;
import com.cjyc.common.model.dto.salesman.mine.StockCarDto;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarDetailVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarVo;

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
    ResultVo<StockCarDetailVo> findStockCarDetail(BaseSalesDto dto);

    /**
     * 业绩统计
     * @param dto
     * @return
     */
    ResultVo achieveCount(AchieveDto dto);
}
