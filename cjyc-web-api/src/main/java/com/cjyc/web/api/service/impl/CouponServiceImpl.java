package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICouponDao;
import com.cjyc.common.model.dao.ICouponSendDao;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.coupon.ConsumeCouponDto;
import com.cjyc.common.model.dto.web.coupon.CouponDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponDto;
import com.cjyc.common.model.entity.Coupon;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.enums.coupon.CouponLifeTypeEnum;
import com.cjyc.common.model.enums.coupon.CouponTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.coupon.ConsumeCouponVo;
import com.cjyc.common.model.vo.web.coupon.CouponVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.ICouponService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/16 10:08
 *  @Description:优惠券
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CouponServiceImpl implements ICouponService {

    @Resource
    private ICouponDao couponDao;

    @Resource
    private ICouponSendDao couponSendDao;

    @Override
    public boolean saveCoupon(CouponDto dto) {
        try{
            Coupon coupon = new Coupon();
            coupon = encapCoupon(coupon,dto);
            coupon.setState(CommonStateEnum.WAIT_CHECK.code);
            coupon.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
            return couponDao.insert(coupon) > 0 ? true : false;
        }catch (Exception e){
            log.info("新增优惠券出现异常");
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public boolean updateCoupon(CouponDto dto) {
        try{
            Coupon coupon = couponDao.selectById(dto.getId());
            if(coupon != null){
                coupon = encapCoupon(coupon,dto);
                return couponDao.updateById(coupon) > 0 ? true : false;
            }
        }catch (Exception e){
            log.info("根据id更新优惠券出现异常");
        }
        return false;
    }

    @Override
    public ResultVo operateCoupon(OperateDto dto) {
        try{
            int n ;
            Coupon coupon = couponDao.selectById(dto.getId());
            if(coupon != null){
                if(FlagEnum.AUDIT_PASS.code == dto.getState()){
                    //审核通过
                    coupon.setState(CommonStateEnum.CHECKED.code);
                }else if(FlagEnum.AUDIT_REJECT.code == dto.getState()){
                    //审核拒绝
                    coupon.setState(CommonStateEnum.REJECT.code);
                }else if(FlagEnum.NULLIFY.code == dto.getState()){
                    //作废
                    coupon.setState(CommonStateEnum.DISABLED.code);
                }
                n = couponDao.updateById(coupon);
                if(n > 0){
                    return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg());
                }
            }else{
                return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
            }
        }catch (Exception e){
            log.info("操作优惠券出现异常");
            throw new CommonException(e.getMessage());
        }
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @Override
    public ResultVo getCouponByTerm(SeleCouponDto dto) {
        PageInfo<CouponVo> pageInfo = null;
        try{
            List<CouponVo> couponVos = couponDao.getCouponByTerm(dto);
            if(couponVos.isEmpty()){
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(), Collections.emptyList());
            }else{
                couponVos = encapCouponList(couponVos);
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(couponVos);
                return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
            }
        }catch (Exception e){
            log.info("根据条件筛选优惠券出现异常");
        }
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(), Collections.emptyList());
    }

    @Override
    public ResultVo getConsumeDetail(ConsumeCouponDto dto) {
        PageInfo<ConsumeCouponVo> pageInfo = null;
        try{
            List<ConsumeCouponVo> vos = couponDao.getConsumeDetail(dto);
            if(vos.isEmpty()){
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),Collections.emptyList());
            }else{
                for(ConsumeCouponVo vo : vos){
                    if(StringUtils.isNotBlank(vo.getUseTime())){
                        vo.setUseTime(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getUseTime())),TimePatternConstant.COMPLEX_TIME_FORMAT));
                    }
                    vo.setCouponOffsetFee(vo.getCouponOffsetFee() == null ? BigDecimal.ZERO:vo.getCouponOffsetFee().divide(new BigDecimal(100)));
                    vo.setFinalOrderAmount(vo.getFinalOrderAmount() == null ? BigDecimal.ZERO:vo.getFinalOrderAmount().divide(new BigDecimal(100)));
                }
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(vos);
                return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
            }
        }catch (Exception e){
            log.info("查询消耗明细出现异常");
            return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(),Collections.emptyList());
        }
    }

    /**
     * 封装优惠券
     * @param coupon
     * @param dto
     * @return
     */
    private Coupon encapCoupon(Coupon coupon,CouponDto dto){
       try{
           coupon.setName(dto.getName());
           coupon.setType(dto.getType());
           if(CouponTypeEnum.FULL_CUT.code == dto.getType()){
               coupon.setFullAmount(dto.getFullAmount() == null ? BigDecimal.ZERO:dto.getFullAmount().multiply(new BigDecimal(100)));
               coupon.setCutAmount(dto.getCutAmount().multiply(new BigDecimal(100)));
           }else if(CouponTypeEnum.DIRECT_CUT.code == dto.getType()){
               coupon.setCutAmount(dto.getCutAmount().multiply(new BigDecimal(100)));
           }else if(CouponTypeEnum.DISCOUNT_CUT.code == dto.getType()){
               coupon.setDiscount(dto.getDiscount());
           }
           coupon.setGrantNum(dto.getGrantNum());
           if(CouponLifeTypeEnum.FOREVER.code != dto.getIsForever()){
               coupon.setStartPeriodDate(LocalDateTimeUtil.convertToLong(dto.getStartPeriodDate(),TimePatternConstant.SIMPLE_DATE_FORMAT));
               coupon.setEndPeriodDate(LocalDateTimeUtil.convertToLong(dto.getEndPeriodDate(), TimePatternConstant.SIMPLE_DATE_FORMAT));
           }
           coupon.setIsForever(dto.getIsForever());
       }catch (Exception e){
           log.info("根据id封装优惠券出现异常");
       }
        return coupon;
    }

    /**
     * 封装查询出的优惠券集合
     * @param couponVos
     * @return
     */
    private List<CouponVo> encapCouponList(List<CouponVo> couponVos){
        List<CouponVo> vos = new ArrayList<>();
        try{
            for(CouponVo vo : couponVos){
                if(StringUtils.isNotBlank(vo.getStartPeriodDate())){
                    vo.setStartPeriodDate(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getStartPeriodDate())),TimePatternConstant.SIMPLE_DATE_FORMAT));
                }
                if(StringUtils.isNotBlank(vo.getEndPeriodDate())){
                    vo.setEndPeriodDate(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getEndPeriodDate())),TimePatternConstant.SIMPLE_DATE_FORMAT));
                }
                if(StringUtils.isNotBlank(vo.getCreateTime())){
                    vo.setCreateTime(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getCreateTime())),TimePatternConstant.COMPLEX_TIME_FORMAT));
                }
                //领取张数
                Integer receiveNum = couponSendDao.getReceiveNum(vo.getId(),null);
                if(null != receiveNum){
                    vo.setReceiveNum(receiveNum);
                }else{
                    vo.setReceiveNum(0);
                }
                //消耗张数
                Integer consumeNum = couponSendDao.getReceiveNum(vo.getId(), UseStateEnum.BE_USE.code);
                if(null != consumeNum){
                    vo.setConsumeNum(consumeNum);
                }else{
                    vo.setConsumeNum(0);
                }
                //永久
                if(CouponLifeTypeEnum.FOREVER.code == vo.getIsForever()){
                    //到期作废张数
                    vo.setExpireDeleNum(0);
                    //剩余可用张数
                    vo.setSurplusAvailNum(vo.getGrantNum() - consumeNum);
                }else{
                    //未过期
                    Long now = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());
                    if(now - vo.getExpireDeleNum() > 0){
                        //到期作废张数
                        vo.setExpireDeleNum(0);
                        //剩余可用张数
                        vo.setSurplusAvailNum(vo.getGrantNum() - consumeNum);
                    }else{
                        //到期作废张数
                        vo.setExpireDeleNum(vo.getGrantNum() - consumeNum);
                        //剩余可用张数
                        vo.setSurplusAvailNum(0);
                    }
                }
                vos.add(vo);
            }
        }catch (Exception e){
            log.info("封装查询出的优惠券集合出现异常");
        }
        return vos;
    }
}