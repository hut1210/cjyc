package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.driver.*;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ExistDriverVo;
import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IDriverService {
    /**
     * 查询司机列表
     * @author JPG
     * @since 2019/10/16 16:15
     */
    ResultVo<PageVo<DriverListVo>> lineWaitDispatchCarCountList(DriverListDto paramsDto);

    /**
     * 保存散户司机
     * @param dto
     * @return
     */
    ResultVo saveOrModifyDriver(DriverDto dto);

    /**
     * 根据条件查询司机信息
     * @param dto
     * @return
     */
    ResultVo<PageVo<DriverVo>> findDriver(SelectDriverDto dto);

    /**
     * 根据司机userId进行审核
     * @param dto
     * @return
     */
    ResultVo verifyDriver(OperateDto dto);

    /**
     * 根据承运商id查看司机信息
     * @param dto
     * @return
     */
    ResultVo showDriver(BaseCarrierIdDto dto);

    /**
     * app校验注册记录
     * @return
     */
    ResultVo<List<ExistDriverVo>> showExistDriver();

    Driver getByUserId(Long userId);

    /**
     * 冻结/解除司机状态
     * @param id
     * @param flag
     * @return
     */
    ResultVo resetState(Long id, Integer flag);

    Driver getById(Long id, boolean isSearchCache);

    ResultVo<PageVo<DispatchDriverVo>> carrierDrvierList(CarrierDriverListDto dto);

    /**
     * 导出司机信息至excel
     * @param request
     * @param response
     */
    void exportDriverExcel(HttpServletRequest request, HttpServletResponse response);



    /************************************韵车集成改版 st***********************************/

    /**
     * 保存社会司机
     * @param dto
     * @return
     */
    ResultVo saveOrModifyDriverNew(DriverDto dto);

    /**
     * 查询社会司机
     * @param dto
     * @return
     */
    ResultVo<PageVo<DriverVo>> findDriverNew(SelectDriverDto dto);

    /**
     * 根据承运商id查看社会司机信息
     * @param dto
     * @return
     */
    ResultVo<ShowDriverVo> showDriverNew(BaseCarrierIdDto dto);

    /**
     * 根据id进行审核通过/拒绝/冻结/解冻_改版
     * @param dto
     * @return
     */
    ResultVo verifyDriverNew(OperateDto dto);

    /**
     * 查询承运商下属司机
     * @param dto
     * @return
     */
    ResultVo<PageVo<DispatchDriverVo>> carrierDrvierListNew(CarrierDriverListDto dto);


    /**
     * 社会司机导入Excel文件
     * @param file
     * @return
     */
    boolean importDriverExcel(MultipartFile file, Long loginId);

    /**
     * 根据司机id 承运商id，手机号删除司机信息
     * @param dto
     * @return
     */
    ResultVo deleteDriverInfo(DeleteDriverDto dto);

    /**
     * 社会司机转到指定的承运商下
     * @param dto
     * @return
     */
    ResultVo changeCarrierSubDriver(CarrierSubDriverDto dto);
}
