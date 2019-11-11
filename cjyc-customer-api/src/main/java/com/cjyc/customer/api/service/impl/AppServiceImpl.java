package com.cjyc.customer.api.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.IDictionaryDao;
import com.cjyc.common.model.entity.Dictionary;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IAppService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leo on 2019/7/25.
 */
@Service
public class AppServiceImpl implements IAppService {

    @Resource
    private IDictionaryDao dictionaryDao;

    @Override
    public ResultVo<List<Dictionary>> getSysPicture(String systemPicture) {
        LambdaQueryWrapper<Dictionary> queryWrapper = new QueryWrapper<Dictionary>().lambda()
                .eq(Dictionary::getItem,systemPicture).eq(Dictionary::getState,1);
        List<Dictionary> dictionaryList = dictionaryDao.selectList(queryWrapper);
        return BaseResultUtil.success(dictionaryList);
    }

}
