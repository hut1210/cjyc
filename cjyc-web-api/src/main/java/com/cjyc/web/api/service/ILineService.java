package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.dto.web.line.AddAndUpdateLineDto;
import com.cjyc.common.model.dto.web.line.ListLineDto;
import com.cjyc.common.model.dto.web.line.SortNodeListDto;
import com.cjyc.common.model.entity.Line;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.line.LineVo;

import java.util.List;

/**
 * <p>
 * 班线管理 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-18
 */
public interface ILineService extends IService<Line> {

    ResultVo<String> sortNode(SortNodeListDto paramsDtoList);

    /**
     * 根据条件分页查询班线
     * @param dto
     * @return
     */
    ResultVo<PageVo<LineVo>> getLineByTerm(SelectInquiryDto dto);

    /**
     * 新增/更新班线
     * @param dto
     * @return
     */
    ResultVo addAndUpdateLine(AddAndUpdateLineDto dto);

    /**
     * 批量删除班线
     * @param lineIds
     * @return
     */
    ResultVo deleteLineByIds(List<Long> lineIds);

    /**
     * 根据起始目的地code查询物流费
     * @param fromCode
     * @param toCode
     * @return
     */
    ResultVo getDefaultWlFeeByCode(String fromCode,String toCode);

    ResultVo<List<Line>> listByTwoCity(ListLineDto reqDto);
}
