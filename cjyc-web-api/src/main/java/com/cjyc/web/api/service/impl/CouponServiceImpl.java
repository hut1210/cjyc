package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICouponDao;
import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.web.coupon.CouponDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponDto;
import com.cjyc.common.model.entity.Coupon;
import com.cjyc.common.model.enums.SysEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
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
    private ICouponDao iCouponDao;

    @Override
    public boolean saveCoupon(CouponDto dto) {
        try{
            Coupon coupon = new Coupon();
            coupon = encapCoupon(coupon,dto);
            coupon.setReceiveNum(0);
            coupon.setConsumeNum(0);
            coupon.setExpireDeleNum(0);
            coupon.setState(0);
            coupon.setCreateTime(LocalDateTimeUtil.convertToLong(LocalDateTimeUtil.formatLDTNow(TimePatternConstant.COMPLEX_TIME_FORMAT), TimePatternConstant.COMPLEX_TIME_FORMAT));
            return iCouponDao.insert(coupon) > 0 ? true : false;
        }catch (Exception e){
            log.info("新增优惠券出现异常");
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public PageInfo<CouponVo> getAllCoupon(BasePageDto dto) {
        PageInfo<CouponVo> pageInfo = null;
        try{
            List<CouponVo> couponVos = iCouponDao.getAllCoupon();
            PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
            pageInfo = encapCouponList(couponVos);
        }catch (Exception e){
            log.info("分页查看优惠券出现异常");
        }
        return pageInfo;
    }

    @Override
    public boolean abolishCouponByIds(List<Long> ids) {
        try{
            int no = 0;
            for(Long id : ids){
                Coupon coupon = iCouponDao.selectById(id);
                coupon.setState(7);
                int num = iCouponDao.updateById(coupon);
                if(num > 0){
                    no ++;
                }
            }
            if(ids.size() == no){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            log.info("作废优惠券出现异常");
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public Coupon showCouponById(Long id) {
        try{
            Coupon coupon = iCouponDao.selectById(id);
            if(coupon.getFullAmount() != null){
                coupon.setFullAmount(coupon.getFullAmount().divide(new BigDecimal(100)));
            }
            if(coupon.getCutAmount() != null){
                coupon.setCutAmount(coupon.getCutAmount().divide(new BigDecimal(100)));
            }
            if(coupon.getStartPeriodDate() != null){
                coupon.setStartPeriodDate(Long.valueOf(LocalDateTimeUtil.convertToString(coupon.getStartPeriodDate(), TimePatternConstant.SIMPLE_DATE_FORMAT)));
            }
            if(coupon.getEndPeriodDate() != null){
                coupon.setEndPeriodDate(Long.valueOf(LocalDateTimeUtil.convertToString(coupon.getEndPeriodDate(), TimePatternConstant.SIMPLE_DATE_FORMAT)));
            }
            return coupon;
        }catch (Exception e){
            log.info("根据id查看优惠券出现异常");
        }
        return null;
    }

    @Override
    public boolean verifyCouponById(Long id, String state) {
        try{
            Coupon coupon = iCouponDao.selectById(id);
            if(coupon != null){
                coupon.setState(Integer.valueOf(state));
                return iCouponDao.updateById(coupon) > 0 ? true : false;
            }
        }catch (Exception e){
            log.info("根据id审核优惠券出现异常");
        }
        return false;
    }

    @Override
    public boolean updateCoupon(CouponDto dto) {
        try{
            Coupon coupon = iCouponDao.selectById(dto.getId());
            if(coupon != null){
                coupon = encapCoupon(coupon,dto);
                return iCouponDao.updateById(coupon) > 0 ? true : false;
            }
        }catch (Exception e){
            log.info("根据id更新优惠券出现异常");
        }
        return false;
    }

    @Override
    public PageInfo<CouponVo> getCouponByTerm(SeleCouponDto dto) {
        PageInfo<CouponVo> pageInfo = null;
        try{
            if(StringUtils.isNotBlank(dto.getStartTime()) && StringUtils.isNotBlank(dto.getEndTime())){
                dto.setStartTime(LocalDateTimeUtil.convertToLong(dto.getStartTime(), TimePatternConstant.SIMPLE_DATE_FORMAT).toString());
                dto.setEndTime(LocalDateTimeUtil.convertToLong(dto.getEndTime(), TimePatternConstant.SIMPLE_DATE_FORMAT).toString());
            }
            List<CouponVo> couponVos = iCouponDao.getCouponByTerm(dto);

            PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
            pageInfo = encapCouponList(couponVos);
        }catch (Exception e){
            log.info("根据条件筛选优惠券出现异常");
        }
        return pageInfo;
    }


    /**
     * 封装优惠券
     * @param coupon
     * @param dto
     * @return
     */
    private Coupon encapCoupon(Coupon coupon,CouponDto dto){
       try{
           coupon.setCouponName(dto.getCouponName());
           coupon.setCouponType(Integer.valueOf(dto.getCouponType()));
           if(dto.getCouponType().equals(SysEnum.ZERO.getValue())){
               coupon.setFullAmount(new BigDecimal(dto.getFullAmount()).multiply(new BigDecimal(100)));
               coupon.setCutAmount(new BigDecimal(dto.getCutAmount()).multiply(new BigDecimal(100)));
           }else if(dto.getCouponType().equals(SysEnum.THREE.getValue())){
               coupon.setCutAmount(new BigDecimal(dto.getCutAmount()).multiply(new BigDecimal(100)));
           }else if(dto.getCouponType().equals(SysEnum.FIVE.getValue())){
               coupon.setDiscount(dto.getDiscount());
           }
           coupon.setGrantNum(Integer.valueOf(dto.getGrantNum()));
           coupon.setSurplusAvailNum(Integer.valueOf(dto.getGrantNum()));
           if(!dto.getIsForever()){
               coupon.setStartPeriodDate(LocalDateTimeUtil.convertToLong(dto.getStartPeriodDate(), TimePatternConstant.SIMPLE_DATE_FORMAT));
               coupon.setEndPeriodDate(LocalDateTimeUtil.convertToLong(dto.getEndPeriodDate(), TimePatternConstant.SIMPLE_DATE_FORMAT));
               coupon.setIsForever(0);
           }else{
               coupon.setIsForever(1);
           }
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
    private PageInfo<CouponVo> encapCouponList(List<CouponVo> couponVos){
        PageInfo<CouponVo> pageInfo = new PageInfo<>();
        try{
            if(couponVos != null && couponVos.size() > 0){
                for(CouponVo vo : couponVos){
                    if(StringUtils.isBlank(vo.getIsForever())){
                        vo.setStartPeriodDate(LocalDateTimeUtil.convertToString(Long.valueOf(vo.getStartPeriodDate()), TimePatternConstant.SIMPLE_DATE_FORMAT));
                        vo.setEndPeriodDate(LocalDateTimeUtil.convertToString(Long.valueOf(vo.getEndPeriodDate()), TimePatternConstant.SIMPLE_DATE_FORMAT));
                    }
                    vo.setCreateTime(LocalDateTimeUtil.convertToString(Long.valueOf(vo.getCreateTime()), TimePatternConstant.COMPLEX_TIME_FORMAT));
                    if(StringUtils.isNotBlank(vo.getFullAmount())){
                        vo.setFullAmount(new BigDecimal(vo.getFullAmount()).divide(new BigDecimal(100)).toString());
                    }
                    if(StringUtils.isNotBlank(vo.getCutAmount())){
                        vo.setCutAmount(new BigDecimal(vo.getCutAmount()).divide(new BigDecimal(100)).toString());
                    }
                }
                pageInfo = new PageInfo<>(couponVos);
            }
        }catch (Exception e){
            log.info("封装查询出的优惠券集合出现异常");
        }
        return pageInfo;
    }
}