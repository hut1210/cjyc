package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.IDictionaryDao;
import com.cjyc.common.model.dto.AppItemDto;
import com.cjyc.common.model.dto.sys.SysPictureDto;
import com.cjyc.common.model.entity.Dictionary;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.AppItemVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.ItemVo;
import com.cjyc.common.system.service.ICsAppService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsAppServiceImpl implements ICsAppService {

    @Resource
    private IDictionaryDao dictionaryDao;

    @Override
    public ResultVo<AppItemVo> getSysPicture(AppItemDto dto) {
        LambdaQueryWrapper<Dictionary> queryWrapper = new QueryWrapper<Dictionary>().lambda()
                .eq(Dictionary::getItem,dto.getSystemPicture()).eq(Dictionary::getState,1).select(Dictionary::getItemValue);
        List<Dictionary> dictionaryList = dictionaryDao.selectList(queryWrapper);
        List<ItemVo> list = new ArrayList<>(5);
        if (!CollectionUtils.isEmpty(dictionaryList)) {
            for(Dictionary dictionary : dictionaryList) {
                ItemVo vo = new ItemVo();
                vo.setPictureUrl("");
                vo.setUrl(dictionary.getItemValue());
                list.add(vo);
            }
        }
        AppItemVo appItemVo = new AppItemVo();
        appItemVo.setAppSystemPicture(list);
        return BaseResultUtil.success(appItemVo);
    }

    @Override
    public ResultVo updateSysPicture(SysPictureDto sysPictureDto) {
        Dictionary dictionary = new Dictionary();
        LambdaQueryWrapper<Dictionary> queryWrapper = new QueryWrapper<Dictionary>().lambda().eq(Dictionary::getItemKey, sysPictureDto.getItemKey());
        int i = dictionaryDao.update(dictionary, queryWrapper);
        if (i < 1) {
            return BaseResultUtil.fail();
        }
        return BaseResultUtil.success();
    }
}