package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.BasePageDto;
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
     * 保存韵车2.0字典
     * @param dictionaryDto
     * @return
     */
    boolean saveDictionary(DictionaryDto dictionaryDto);

    /**
     * 根据主键id查看字典
     * @param id
     * @return
     */
    Dictionary showDictionaryById(Long id);

    /**
     * 根据ids批量删除字典项
     * @param ids
     * @return
     */
    boolean delDictionaryByIds(List<Long> ids);

    /**
     * 根据字典项id更新
     * @param dictionaryDto
     * @return
     */
    boolean updDictionaryById(DictionaryDto dictionaryDto);

    /**
     * 分页查询字典项
     * @param pageDto
     * @return
     */
    PageInfo<Dictionary> getAllDictionary(BasePageDto pageDto);

    /**
     * 根据条件查询字典项
     * @param dto
     * @return
     */
    PageInfo<Dictionary> findDictionary(SelectDictionaryDto dto);

    ResultVo<Map<String, Object>> getInsurance(int valuation);
}
