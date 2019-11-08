package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.entity.Inquiry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.inquiry.InquiryVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
 */
public interface IInquiryDao extends BaseMapper<Inquiry> {

    /**
     * 根据条件查询询价管理条目
     * @param dto
     * @return
     */
    List<InquiryVo> findInquiry(SelectInquiryDto dto);

}
