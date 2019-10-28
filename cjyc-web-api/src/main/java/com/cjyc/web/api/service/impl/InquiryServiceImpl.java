package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.entity.Inquiry;
import com.cjyc.common.model.dao.IInquiryDao;
import com.cjyc.common.model.enums.inquiry.InquiryStateEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.web.inquiry.InquiryVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.IInquiryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class InquiryServiceImpl extends ServiceImpl<IInquiryDao, Inquiry> implements IInquiryService {

    @Resource
    private IInquiryDao iInquiryDao;

    @Override
    public PageInfo<InquiryVo> getAllInquiryByTerm(SelectInquiryDto dto) {
        PageInfo<InquiryVo> pageInfo = null;
        try{
            //日期转Long
            if(StringUtils.isNotBlank(dto.getStartDate())){
                dto.setStartStamp(LocalDateTimeUtil.convertToLong(dto.getStartDate(),TimePatternConstant.SIMPLE_DATE_FORMAT));
            }
            if(StringUtils.isNotBlank(dto.getEndDate())){
                dto.setEndStamp(LocalDateTimeUtil.convertToLong(dto.getEndDate(),TimePatternConstant.SIMPLE_DATE_FORMAT));
            }
            List<InquiryVo> inquiryVos = iInquiryDao.getAllInquiryByTerm(dto);
            if(inquiryVos != null && inquiryVos.size() > 0){
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
                    PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                    pageInfo = new PageInfo<>(inquiryVos);
                }
            }
        }catch (Exception e){
            log.info("根据条件查询询价条目出现异常");
            throw new CommonException(e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public boolean addJobContentById(Long inquiryId,String jobContent) {
        try{
            Inquiry inquiry = iInquiryDao.selectById(inquiryId);
            if(inquiry != null){
                inquiry.setJobContent(jobContent);
                return iInquiryDao.updateById(inquiry) > 0 ? true:false;
            }
        }catch (Exception e){
            log.info("根据id新增工单出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }
}
