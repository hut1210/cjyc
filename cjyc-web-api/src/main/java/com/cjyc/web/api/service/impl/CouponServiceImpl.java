package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.dao.ICouponDao;
import com.cjyc.common.model.dao.ICouponSendDao;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.coupon.ConsumeCouponDto;
import com.cjyc.common.model.dto.web.coupon.CouponDto;
import com.cjyc.common.model.dto.web.coupon.SeleCouponDto;
import com.cjyc.common.model.entity.Coupon;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.enums.coupon.CouponLifeTypeEnum;
import com.cjyc.common.model.enums.coupon.CouponTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.coupon.ConsumeCouponVo;
import com.cjyc.common.model.vo.web.coupon.CouponExportExcel;
import com.cjyc.common.model.vo.web.coupon.CouponVo;
import com.cjyc.web.api.service.ICouponService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/16 10:08
 *  @Description:优惠券
 */
@Service
@Slf4j
public class CouponServiceImpl extends ServiceImpl<ICouponDao,Coupon> implements ICouponService {

    @Resource
    private ICouponDao couponDao;

    @Resource
    private ICouponSendDao couponSendDao;

    private Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo saveOrModifyCoupon(CouponDto dto) {
        if(dto.getCouponId() == null){
            //保存
            Coupon coupon = new Coupon();
            coupon = encapCoupon(coupon,dto);
            coupon.setId(dto.getCouponId());
            coupon.setState(CommonStateEnum.WAIT_CHECK.code);
            coupon.setCreateTime(NOW);
            coupon.setCreateUserId(dto.getLoginId());
            couponDao.insert(coupon);
        }else{
            //修改
            Coupon coupon = new Coupon();
            coupon = encapCoupon(coupon,dto);
            super.updateById(coupon);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo verifyCoupon(OperateDto dto) {
         Coupon coupon = new Coupon();
         BeanUtils.copyProperties(dto,coupon);
        if(FlagEnum.AUDIT_PASS.code == dto.getFlag()){
            //审核通过
            coupon.setState(CommonStateEnum.CHECKED.code);
        }else if(FlagEnum.AUDIT_REJECT.code == dto.getFlag()){
            //审核拒绝
            coupon.setState(CommonStateEnum.REJECT.code);
        }else if(FlagEnum.NULLIFY.code == dto.getFlag()){
            //作废
            coupon.setState(CommonStateEnum.DISABLED.code);
        }
        super.updateById(coupon);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<CouponVo>> getCouponByTerm(SeleCouponDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        if(dto.getEndTime() != null){
            dto.setEndTime(TimeStampUtil.addDays(dto.getEndTime(),1));
        }
        List<CouponVo> couponVos = couponDao.getCouponByTerm(dto);
        PageInfo<CouponVo> pageInfo = new PageInfo<>(couponVos);
        List<CouponVo> pageInfoList = pageInfo.getList();
        if(!CollectionUtils.isEmpty(pageInfoList)){
            couponVos = encapCouponList(pageInfoList);
        }
        pageInfo.setList(couponVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<ConsumeCouponVo>> getConsumeDetail(ConsumeCouponDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<ConsumeCouponVo> vos = couponDao.getConsumeDetail(dto);
        PageInfo<ConsumeCouponVo> pageInfo = new PageInfo<>(vos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public void exportCouponExcel(HttpServletRequest request, HttpServletResponse response) {
        SeleCouponDto dto = getCouponDto(request);
        List<CouponVo> couponVos = couponDao.getCouponByTerm(dto);
        if(!CollectionUtils.isEmpty(couponVos)){
            couponVos = encapCouponList(couponVos);
        }
        if (!CollectionUtils.isEmpty(couponVos)) {
            // 生成导出数据
            List<CouponExportExcel> exportExcelList = new ArrayList<>();
            for (CouponVo vo : couponVos) {
                CouponExportExcel couponExportExcel = new CouponExportExcel();
                BeanUtils.copyProperties(vo, couponExportExcel);
                exportExcelList.add(couponExportExcel);
            }
            String title = "优惠券管理";
            String sheetName = "优惠券管理";
            String fileName = "优惠券管理.xls";
            try {
                if(!CollectionUtils.isEmpty(exportExcelList)){
                    ExcelUtil.exportExcel(exportExcelList, title, sheetName, CouponExportExcel.class, fileName, response);
                }
            } catch (IOException e) {
                log.error("导出优惠券管理信息异常:{}",e);
            }
        }
    }

    /**
     * 封装优惠券excel请求
     * @param request
     * @return
     */
    private SeleCouponDto getCouponDto(HttpServletRequest request){
        SeleCouponDto dto = new SeleCouponDto();
        dto.setName(request.getParameter("name"));
        dto.setState(StringUtils.isBlank(request.getParameter("type")) ? null:Integer.valueOf(request.getParameter("type")));
        dto.setState(StringUtils.isBlank(request.getParameter("state")) ? null:Integer.valueOf(request.getParameter("state")));
        dto.setStartTime(StringUtils.isBlank(request.getParameter("startTime")) ? null:Long.valueOf(request.getParameter("startTime")));
        dto.setEndTime(StringUtils.isBlank(request.getParameter("endTime")) ? null:TimeStampUtil.addDays(Long.valueOf(request.getParameter("endTime")),1));
        dto.setCreateName(request.getParameter("createName"));
        return dto;
    }

    /**
     * 封装优惠券
     * @param coupon
     * @param dto
     * @return
     */
    private Coupon encapCoupon(Coupon coupon,CouponDto dto){
        BeanUtils.copyProperties(dto,coupon);
        coupon.setId(dto.getCouponId());
       if(CouponTypeEnum.FULL_CUT.code == coupon.getType()){
           coupon.setFullAmount(coupon.getFullAmount() == null ? BigDecimal.ZERO:coupon.getFullAmount().multiply(new BigDecimal(100)));
           coupon.setCutAmount(coupon.getCutAmount() == null ? BigDecimal.ZERO:coupon.getCutAmount().multiply(new BigDecimal(100)));
       }else if(CouponTypeEnum.DIRECT_CUT.code == coupon.getType()){
           coupon.setCutAmount(coupon.getCutAmount()== null ? BigDecimal.ZERO:coupon.getCutAmount().multiply(new BigDecimal(100)));
       }else if(CouponTypeEnum.DISCOUNT_CUT.code == coupon.getType()){
           coupon.setDiscount(coupon.getDiscount() == null ? BigDecimal.ZERO:coupon.getDiscount());
       }
       if(CouponLifeTypeEnum.FOREVER.code != coupon.getIsForever()){
           coupon.setStartPeriodDate(dto.getStartPeriodDate());
           coupon.setEndPeriodDate(dto.getEndPeriodDate());
       }
        return coupon;
    }

    /**
     * 封装查询出的优惠券集合
     * @param couponVos
     * @return
     */
    private List<CouponVo> encapCouponList(List<CouponVo> couponVos){
        List<CouponVo> vos = new ArrayList<>(10);
        for(CouponVo vo : couponVos){
            //领取张数
            Integer receiveNum = couponSendDao.getReceiveNum(vo.getCouponId(),null);
            if(null != receiveNum){
                vo.setReceiveNum(receiveNum);
            }else{
                vo.setReceiveNum(0);
            }
            //消耗张数
            Integer consumeNum = couponSendDao.getReceiveNum(vo.getCouponId(), UseStateEnum.BE_USE.code);
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
                if(NOW > vo.getEndPeriodDate()){
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
        return vos;
    }
}