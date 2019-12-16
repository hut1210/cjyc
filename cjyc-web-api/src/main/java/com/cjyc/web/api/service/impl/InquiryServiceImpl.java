package com.cjyc.web.api.service.impl;

import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.dto.web.inquiry.HandleInquiryDto;
import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.entity.Inquiry;
import com.cjyc.common.model.dao.IInquiryDao;
import com.cjyc.common.model.enums.inquiry.InquiryStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.inquiry.InquiryExportExcel;
import com.cjyc.common.model.vo.web.inquiry.InquiryVo;
import com.cjyc.web.api.service.IInquiryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<InquiryVo> inquiryVos = encapInquiry(dto);
        PageInfo<InquiryVo> pageInfo = new PageInfo<>(inquiryVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public boolean handleInquiry(HandleInquiryDto dto) {
        Inquiry inquiry = new Inquiry();
        BeanUtils.copyProperties(dto,inquiry);
        inquiry.setState(InquiryStateEnum.YES_HANDLE.code);
        inquiry.setHandleTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
        return super.updateById(inquiry);
    }

    @Override
    public void exportInquiryExcel(HttpServletRequest request, HttpServletResponse response) {
        SelectInquiryDto dto = getInquiryDto(request);
        List<InquiryVo> inquiryVos = encapInquiry(dto);
        if (!CollectionUtils.isEmpty(inquiryVos)) {
            // 生成导出数据
            List<InquiryExportExcel> exportExcelList = new ArrayList<>();
            for (InquiryVo vo : inquiryVos) {
                InquiryExportExcel inquiryExportExcel = new InquiryExportExcel();
                BeanUtils.copyProperties(vo, inquiryExportExcel);
                exportExcelList.add(inquiryExportExcel);
            }
            String title = "询价管理";
            String sheetName = "询价管理";
            String fileName = "询价管理.xls";
            try {
                if(!CollectionUtils.isEmpty(exportExcelList)){
                    ExcelUtil.exportExcel(exportExcelList, title, sheetName, InquiryExportExcel.class, fileName, response);
                }
            } catch (IOException e) {
                log.error("导出询价管理信息异常:{}",e);
            }
        }
    }

    /**
     * 封装询价excel请求
     * @param request
     * @return
     */
    private SelectInquiryDto getInquiryDto(HttpServletRequest request){
        SelectInquiryDto dto = new SelectInquiryDto();
        dto.setFromCity(request.getParameter("fromCity"));
        dto.setToCity(request.getParameter("toCity"));
        dto.setState(StringUtils.isBlank(request.getParameter("state")) ? null:Integer.valueOf(request.getParameter("state")));
        dto.setStartStamp(StringUtils.isBlank(request.getParameter("startStamp")) ? null:Long.valueOf(request.getParameter("startStamp")));
        dto.setEndStamp(StringUtils.isBlank(request.getParameter("endStamp")) ? null:TimeStampUtil.addDays(Long.valueOf(request.getParameter("endStamp")),1));
        return dto;
    }

    /**
     * 封装询价
     * @param dto
     * @return
     */
    private List<InquiryVo> encapInquiry(SelectInquiryDto dto){
        if(dto.getEndStamp() != null){
            dto.setEndStamp(TimeStampUtil.addDays(dto.getEndStamp(),1));
        }
        List<InquiryVo> inquiryVos = inquiryDao.findInquiry(dto);
        if(!CollectionUtils.isEmpty(inquiryVos)){
            for(InquiryVo vo : inquiryVos){
                if(InquiryStateEnum.YES_HANDLE.code == vo.getState()){
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
        return inquiryVos;
    }
}
