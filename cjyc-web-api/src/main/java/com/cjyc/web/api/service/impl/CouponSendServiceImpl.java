package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICouponSendDao;
import com.cjyc.common.model.dto.web.coupon.CouponSendDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponSendDto;
import com.cjyc.common.model.entity.CouponSend;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.web.coupon.CouponSendVo;
import com.cjyc.web.api.service.ICouponSendService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/16 10:08
 *  @Description:优惠券发放
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CouponSendServiceImpl implements ICouponSendService {

    @Resource
    private ICouponSendDao iCouponSendDao;

    @Override
    public PageInfo<CouponSendVo> seleCouponSendByTerm(SeleCouponSendDto dto) {
        PageInfo<CouponSendVo> pageInfo = new PageInfo<>();
        try{
            List<CouponSendVo> couponSendVos = iCouponSendDao.seleCouponSendByTerm(dto);
            if(couponSendVos != null && couponSendVos.size() > 0){
                for(CouponSendVo vo : couponSendVos){
                    LocalDateTimeUtil.convertToString(Long.valueOf(vo.getUseTime()), TimePatternConstant.COMPLEX_TIME_FORMAT);
                }
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(couponSendVos);
            }
        }catch (Exception e){
            log.info("查询发放优惠券出现异常");
        }
        return pageInfo;
    }

    @Override
    public PageInfo<CouponSend> seleCouponSendByIds(CouponSendDto dto) {
        PageInfo<CouponSend> pageInfo = new PageInfo<>();
        try{
            List<CouponSend> couponSends = iCouponSendDao.selectBatchIds(dto.getIds());
            if(couponSends != null && couponSends.size() > 0){
                for(CouponSend vo : couponSends){
                    LocalDateTimeUtil.convertToString(Long.valueOf(vo.getUseTime()), TimePatternConstant.COMPLEX_TIME_FORMAT);
                }
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(couponSends);
            }
        }catch (Exception e){
            log.info("查询发放优惠券出现异常");
        }
        return pageInfo;
    }
}