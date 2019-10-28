package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.entity.Inquiry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.web.inquiry.InquiryVo;
import com.github.pagehelper.PageInfo;

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
    PageInfo<InquiryVo> getAllInquiryByTerm(SelectInquiryDto dto);

    /**
     * 根据询价id新增工单
     * @param inquiryId
     * @return
     */
    boolean addJobContentById(Long inquiryId,String jobContent);

}
