package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.constant.PatternConstant;
import com.cjyc.common.model.dao.IDictionaryDao;
import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.web.DictionaryDto;
import com.cjyc.common.model.dto.web.SelectDictionaryDto;
import com.cjyc.common.model.entity.Dictionary;
import com.cjyc.common.model.enums.SysEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.IDictionaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/12 9:48
 *  @Description:字典维护业务实现
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class DictionaryServiceImpl implements IDictionaryService {

    @Resource
    private IDictionaryDao iDictionaryDao;

    @Override
    public boolean saveDictionary(DictionaryDto dto) {
        try{
            Dictionary dic = new Dictionary();
            dic.setName(dto.getName());
            dic.setItem(dto.getItem());
            dic.setItemKey(dto.getItemKey());
            dic.setItemValue(dto.getItemValue());
            dic.setItemUnit(dto.getItemUnit());
            dic.setFixedFlag(SysEnum.ZERO.getValue());
            dic.setRemark(dto.getRemark());
            dic.setState(SysEnum.ONE.getValue());
            dic.setCreateTime(LocalDateTimeUtil.convertToLong(LocalDateTimeUtil.formatLDTNow(PatternConstant.COMPLEX_TIME_FORMAT),PatternConstant.COMPLEX_TIME_FORMAT));
            return iDictionaryDao.insert(dic) > 0 ? true:false;
        }catch (Exception e){
            log.info("保存字典项出现异常");
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public Dictionary showDictionaryById(Long id) {
        Dictionary dic = null;
        try{
            if(id != null){
                dic = iDictionaryDao.selectById(id);
            }
        }catch (Exception e){
            log.info("根据id查看字典项出现异常");
        }
        return dic;
    }

    @Override
    public boolean delDictionaryByIds(List<Long> ids) {
        try{
            if(ids != null && ids.size() > 0){
                return iDictionaryDao.deleteBatchIds(ids) > 0 ? true:false;
            }
        }catch (Exception e){
            log.info("根据ids批量删除字典项出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updDictionaryById(DictionaryDto dto) {
        try{
            Dictionary dic = iDictionaryDao.selectById(dto.getId());
            if(dic != null){
                dic.setName(dto.getName());
                dic.setItem(dto.getItem());
                dic.setItemKey(dto.getItemKey());
                dic.setItemValue(dto.getItemValue());
                dic.setItemUnit(dto.getItemUnit());
                dic.setRemark(dto.getRemark());
                return iDictionaryDao.updateById(dic) > 0 ? true:false;
            }
        }catch (Exception e){
            log.info("根据id更新字典项出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public PageInfo<Dictionary> getAllDictionary(BasePageDto pageDto) {
        PageInfo<Dictionary> pageInfo = null;
        try{
            if(pageDto.getCurrentPage() == null || pageDto.getCurrentPage() < 1){
                pageDto.setCurrentPage(1);
            }
            if(pageDto.getPageSize() == null || pageDto.getPageSize() < 1){
                pageDto.setPageSize(10);
            }
            PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
            List<Dictionary> dictionaryList = iDictionaryDao.selectList(new QueryWrapper<>());
            pageInfo = new PageInfo<>(dictionaryList);
        }catch (Exception e){
            log.info("根据id更新字典项出现异常");
        }
        return pageInfo;
    }

    @Override
    public PageInfo<Dictionary> findDictionary(SelectDictionaryDto dto) {
        PageInfo<Dictionary> pageInfo = null;
        try{
            if(dto.getCurrentPage() == null || dto.getCurrentPage() < 1){
                dto.setCurrentPage(1);
            }
            if(dto.getPageSize() == null || dto.getPageSize() < 1){
                dto.setPageSize(10);
            }
            PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
            List<Dictionary> dictionaryList = iDictionaryDao.selectList(new QueryWrapper<Dictionary>().eq("name",dto.getName()));
            pageInfo = new PageInfo<>(dictionaryList);
        }catch (Exception e){
            log.info("根据id更新字典项出现异常");
        }
        return pageInfo;
    }
}