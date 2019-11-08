package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.IDictionaryDao;
import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.dictionary.DictionaryDto;
import com.cjyc.common.model.dto.web.dictionary.SelectDictionaryDto;
import com.cjyc.common.model.entity.Dictionary;
import com.cjyc.common.model.enums.DictionaryEnum;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.IDictionaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  @author: zj
 *  @Date: 2019/10/12 9:48
 *  @Description:字典维护业务实现
 */
@Service
@Slf4j
public class DictionaryServiceImpl extends ServiceImpl<IDictionaryDao,Dictionary> implements IDictionaryService {

    @Resource
    private IDictionaryDao dictionaryDao;

    @Override
    public boolean modify(DictionaryDto dto) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dto,dictionary);
        return super.updateById(dictionary);
    }

    @Override
    public ResultVo queryPage(SelectDictionaryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<Dictionary> dictionaryList = dictionaryDao.selectList(new QueryWrapper<Dictionary>().lambda().
                eq(StringUtils.isNotBlank(dto.getName()),Dictionary::getName,dto.getName()));
        PageInfo<Dictionary> pageInfo = new PageInfo<>(dictionaryList);
        return BaseResultUtil.success(pageInfo == null ? new PageInfo<>():pageInfo);
    }

    @Override
    public ResultVo<Map<String, Object>> getInsurance(int valuation) {
        int add_insurance_amount = 0;
        int add_insurance_fee = 0;

        Integer baseAmount = 0;
        Dictionary dictionary = findByEnum(DictionaryEnum.INSURANCE_BASE_AMOUNT);
        if(dictionary != null || dictionary.getItemValue() != null){
            baseAmount = Integer.valueOf(dictionary.getItemValue());
        }

        //追加保额等于估值-
        if(valuation > baseAmount){
            add_insurance_amount = valuation - baseAmount <= 0 ? 0 : valuation - baseAmount;
            //追加保费
            if(valuation > 10 && valuation <= 15){
                add_insurance_fee = 12;
            }else if(valuation > 15 && valuation <= 30){
                add_insurance_fee = 17;
            }else if(valuation > 30 && valuation <= 45){
                add_insurance_fee = 32;
            }else if(valuation > 45 && valuation <= 60){
                add_insurance_fee = 52;
            }else if(valuation > 60 && valuation <= 90){
                add_insurance_fee = 70;
            }else{
                add_insurance_fee = 2 * add_insurance_amount;
            }
        }
        if(add_insurance_fee > 500){
            add_insurance_fee = 500;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("add_insurance_amount", add_insurance_amount);
        map.put("add_insurance_fee", add_insurance_fee);
        return BaseResultUtil.success(map);
    }

    @Override
    public ResultVo queryConfig() {
        List<Map<String,String>> mapList = dictionaryDao.getSystemConfig(FieldConstant.ITEM);
        return BaseResultUtil.success(mapList == null ? Collections.EMPTY_LIST:mapList);
    }

    @Override
    public boolean modifyConfig(OperateDto dto) {
        Dictionary dictionary = dictionaryDao.selectById(dto.getId());
        if(dictionary != null){
            if(FlagEnum.TURNOFF_SWITCH.code == dto.getFlag()){
                //关闭开关
                dictionary.setState(UseStateEnum.DISABLED.code);
            }else if(FlagEnum.TURNONN_SWITCH.code == dto.getFlag()){
                //打开开关
                dictionary.setState(UseStateEnum.USABLE.code);
            }
        }
        return dictionaryDao.updateById(dictionary) > 0 ? true:false;
    }


    private Dictionary findByEnum(DictionaryEnum dictionaryEnum) {
        return dictionaryDao.findByItemKey(dictionaryEnum.item, dictionaryEnum.key);
    }
}