package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.entity.CustomerLine;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerLine.CustomerLineVo;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JPG
 * @since 2019-11-01
 */
public interface ICustomerLineService extends IService<CustomerLine> {

    /**
     * 查询用户历史线路
     * @param dto
     * @return
     */
    ResultVo<PageVo<CustomerLineVo>> queryLinePage(CommonDto dto);

}
