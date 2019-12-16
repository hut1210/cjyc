package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.inquiry.HandleInquiryDto;
import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.entity.Inquiry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.inquiry.InquiryVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
 */
public interface IInquiryService extends IService<Inquiry> {

    /**
     * 根据条件查询询价条目
     * @param dto
     * @return
     */
    ResultVo findInquiry(SelectInquiryDto dto);

    /**
     * 处理工单
     * @param dto
     * @return
     */
    boolean handleInquiry(HandleInquiryDto dto);

    /**
     * 询价管理导出至excel
     * @param request
     * @param response
     */
    void exportInquiryExcel(HttpServletRequest request, HttpServletResponse response);

}
