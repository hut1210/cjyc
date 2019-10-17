package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.CouponSend;
import com.cjyc.common.model.vo.customer.couponSend.CouponSendVo;
import com.github.pagehelper.PageInfo;

public interface ICouponSendService extends IService<CouponSend> {

    /**
     * 根据用户id查询用户优惠券明细
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageInfo<CouponSendVo> getCouponSendByUserId(Long userId,Integer currentPage,Integer pageSize);
}
