package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.entity.CouponSend;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.coupon.CustomerCouponVo;
import com.github.pagehelper.PageInfo;

public interface ICustomerCouponService extends IService<CouponSend> {

    /**
     * 查看客户优惠券
     * @param dto
     * @return
     */
    ResultVo customerCoupon(CommonDto dto);
}
