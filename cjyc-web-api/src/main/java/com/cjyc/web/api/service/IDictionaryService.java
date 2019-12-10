package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.dictionary.DictionaryDto;
import com.cjyc.common.model.dto.web.dictionary.SelectDictionaryDto;
import com.cjyc.common.model.entity.Dictionary;
import com.cjyc.common.model.vo.ResultVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 *  @author: zj
 *  @Date: 2019/10/12 9:47
 *  @Description:字典维护接口
 */
public interface IDictionaryService {

    /**
     * 根据字典项id更新
     * @param dictionaryDto
     * @return
     */
    boolean modify(DictionaryDto dictionaryDto);

    /**
     * 根据条件查询字典项
     * @param dto
     * @return
     */
    ResultVo queryPage(SelectDictionaryDto dto);


    /**
     * 查询所有系统配置
     * @return
     */
    ResultVo queryConfig();

    /**
     * 更新开关
     * @param dto
     * @return
     */
    boolean modifyConfig(OperateDto dto);
}
