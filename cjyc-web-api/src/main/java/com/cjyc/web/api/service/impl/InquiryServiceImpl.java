package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dto.web.inquiry.HandleInquiryDto;
import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.entity.Inquiry;
import com.cjyc.common.model.dao.IInquiryDao;
import com.cjyc.common.model.enums.inquiry.InquiryStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.inquiry.InquiryVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.IInquiryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
 */
@Service
@Slf4j
public class InquiryServiceImpl extends ServiceImpl<IInquiryDao, Inquiry> implements IInquiryService {

    @Resource
    private IInquiryDao inquiryDao;

    @Override
    public ResultVo findInquiry(SelectInquiryDto dto) {
        PageInfo<InquiryVo> pageInfo = null;
        //日期转Long
        if(StringUtils.isNotBlank(dto.getStartDate())){
            dto.setStartStamp(LocalDateTimeUtil.convertToLong(dto.getStartDate(),TimePatternConstant.SIMPLE_DATE_FORMAT));
        }
        if(StringUtils.isNotBlank(dto.getEndDate())){
            dto.setEndStamp(LocalDateTimeUtil.convertToLong(dto.getEndDate(),TimePatternConstant.SIMPLE_DATE_FORMAT));
        }
        //当前时间减去一小时
        Long hourAgo = LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.minus(LocalDateTime.now(),
                1,
                ChronoUnit.HOURS));
        dto.setHourAgo(hourAgo);
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<InquiryVo> inquiryVos = inquiryDao.findInquiry(dto);
        if(!CollectionUtils.isEmpty(inquiryVos)){
            for(InquiryVo vo : inquiryVos){
                vo.setHandleTime(StringUtils.isBlank(vo.getHandleTime()) ? "":LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.parseLong(vo.getHandleTime())), TimePatternConstant.COMPLEX_TIME_FORMAT));
                vo.setLogisticsFee(vo.getLogisticsFee() == null ? BigDecimal.ZERO : vo.getLogisticsFee().divide(new BigDecimal(100)));
                if(InquiryStateEnum.YES_HANDLE.name.equals(vo.getState())){
                    continue;
                }
                if(StringUtils.isNotBlank(vo.getInquiryTime())){
                    Long inquiryTime = Long.parseLong(vo.getInquiryTime());
                    LocalDateTime time =  LocalDateTimeUtil.convertLongToLDT(inquiryTime);
                    //判断数据是否标红
                    LocalDateTime currentNow = LocalDateTimeUtil.plus(LocalDateTime.now(),
                            24,
                            ChronoUnit.HOURS);
                    long diffValue = LocalDateTimeUtil.betweenTwoLDT(currentNow,time,ChronoUnit.HOURS);
                    if(diffValue - 2 > 0){
                        vo.setIsRed(InquiryStateEnum.YES_RED.code);
                    }else{
                        vo.setIsRed(InquiryStateEnum.NO_RED.code);
                    }
                    vo.setInquiryTime(StringUtils.isBlank(vo.getInquiryTime()) ? "":LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(inquiryTime), TimePatternConstant.COMPLEX_TIME_FORMAT));
                }
            }
            pageInfo = new PageInfo<>(inquiryVos);
        }
        return BaseResultUtil.success(pageInfo == null ? new PageInfo<>():pageInfo);
    }

    @Override
    public boolean handleInquiry(HandleInquiryDto dto) {
        Inquiry inquiry = new Inquiry();
        BeanUtils.copyProperties(dto,inquiry);
        inquiry.setHandleTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
        return super.updateById(inquiry);
    }
}
