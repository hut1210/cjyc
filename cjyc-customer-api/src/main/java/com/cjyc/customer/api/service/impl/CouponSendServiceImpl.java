package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICouponSendDao;
import com.cjyc.common.model.entity.CouponSend;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.customer.couponSend.CouponSendVo;
import com.cjyc.customer.api.exception.CommonException;
import com.cjyc.customer.api.service.ICouponSendService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CouponSendServiceImpl extends ServiceImpl<ICouponSendDao, CouponSend> implements ICouponSendService {

    @Resource
    private ICouponSendDao iCouponSendDao;

    @Override
    public PageInfo<CouponSendVo> getCouponSendByUserId(Long userId, Integer currentPage, Integer pageSize) {
        PageInfo<CouponSendVo> pageInfo = new PageInfo<>();
        try{
            List<CouponSendVo> sendVos = iCouponSendDao.getCouponSendByUserId(userId);
            if(sendVos != null && sendVos.size() > 0){
                for(CouponSendVo vo : sendVos){
                    if(StringUtils.isBlank(vo.getIsForever())){
                        vo.setPeriodDate(LocalDateTimeUtil.convertToLong(vo.getPeriodDate(), TimePatternConstant.SIMPLE_DATE_FORMAT).toString());
                    }
                }
                PageHelper.startPage(currentPage,pageSize);
                pageInfo = new PageInfo<>(sendVos);
            }
        }catch (Exception e){
            log.info("根据用户id获取优惠券明细出现异常");
            throw new CommonException(e.getMessage());
        }
        return pageInfo;
    }
}