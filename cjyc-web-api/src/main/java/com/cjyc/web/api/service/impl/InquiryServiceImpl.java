package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dto.web.inquiry.HandleInquiryDto;
import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.entity.Inquiry;
import com.cjyc.common.model.dao.IInquiryDao;
import com.cjyc.common.model.enums.inquiry.InquiryStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.inquiry.InquiryVo;
import com.cjyc.web.api.service.IInquiryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
        //日期转Long
        //当前时间减去一小时
        Long hourAgo = LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.minus(LocalDateTime.now(),
                1,
                ChronoUnit.HOURS));
        dto.setHourAgo(hourAgo);
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<InquiryVo> inquiryVos = inquiryDao.findInquiry(dto);
        if(!CollectionUtils.isEmpty(inquiryVos)){
            for(InquiryVo vo : inquiryVos){
                if(InquiryStateEnum.YES_HANDLE.name.equals(vo.getState())){
                    continue;
                }
                if(vo.getInquiryTime() != null){
                    LocalDateTime time =  LocalDateTimeUtil.convertLongToLDT(vo.getInquiryTime());
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
                }
            }
        }
        PageInfo<InquiryVo> pageInfo = new PageInfo<>(inquiryVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public boolean handleInquiry(HandleInquiryDto dto) {
        Inquiry inquiry = new Inquiry();
        BeanUtils.copyProperties(dto,inquiry);
        inquiry.setHandleTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
        return super.updateById(inquiry);
    }
}
