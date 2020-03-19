package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.customer.AppCustomerDto;
import com.cjyc.common.model.dto.web.customer.CustomerfuzzyListDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerInfo.CustomerCardInfoVo;
import com.cjyc.common.model.vo.customer.customerInfo.AppCustomerInfoVo;
import com.cjyc.common.model.vo.web.customer.ShowPartnerVo;

/**
 * <p>
 * 客户表（登录用户端APP用户） 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-18
 */
public interface ICustomerService extends IService<Customer> {

    ResultVo fuzzyList(CustomerfuzzyListDto paramsDto);

    Customer getByUserId(Long userId);

    /**
     * 获取客户信息
     * @param dto
     * @return
     */
    ResultVo<AppCustomerInfoVo> findNewCustomerInfo(AppCustomerDto dto);

    /**
     * 查看合伙人信息
     * @param dto
     * @return
     */
    ResultVo<ShowPartnerVo> showPartner(AppCustomerDto dto);




    /************************************韵车集成改版 st***********************************/

    /**
     * 获取客户信息_改版
     * @param dto
     * @return
     */
    ResultVo<AppCustomerInfoVo> findNewCustomerInfoNew(AppCustomerDto dto);

    /**
     * 获取用户银行卡信息
     * @param dto
     * @return
     */
    ResultVo<CustomerCardInfoVo> findPartnerBank(AppCustomerDto dto);

}
