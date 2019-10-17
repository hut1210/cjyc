package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.coupon.SeleCouponSendDto;
import com.cjyc.common.model.entity.CouponSend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.coupon.CouponSendVo;

import java.util.List;

/**
 * <p>
 * 优惠券主表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-16
 */
public interface ICouponSendDao extends BaseMapper<CouponSend> {

    /**
     * 根据条件查询发放优惠券
     * @param dto
     * @return
     */
    List<CouponSendVo> seleCouponSendByTerm(SeleCouponSendDto dto);
}
