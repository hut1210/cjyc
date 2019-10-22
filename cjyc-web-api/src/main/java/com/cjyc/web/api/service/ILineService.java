package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.line.SortNodeListDto;
import com.cjyc.common.model.entity.Line;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;

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
}
