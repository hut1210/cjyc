package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dto.web.config.ConfigDto;
import com.cjyc.common.model.entity.Config;
import com.cjyc.common.model.dao.IConfigDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.IConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-29
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class ConfigServiceImpl extends ServiceImpl<IConfigDao, Config> implements IConfigService {

    @Resource
    private IConfigDao configDao;

    @Override
    public ResultVo queryConfig(ConfigDto dto) {
        PageInfo<Config> pageInfo = null;
        try{
            if(FlagEnum.QUERY.code == dto.getFlag()){
               List<Config> configs = configDao.getAllConfig();
               if(!configs.isEmpty() && !CollectionUtils.isEmpty(configs)){
                   PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                   pageInfo = new PageInfo<>(configs);
                   return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
               }
            }
        }catch (Exception e){
            log.info("查询系统配置出现异常");
            throw new CommonException(e.getMessage());
        }
        return BaseResultUtil.getPageVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(),pageInfo);
    }

    @Override
    public ResultVo updateConfig(ConfigDto dto) {
        int n = 0;
        try{
            if(FlagEnum.UPDTATE.code == dto.getFlag()){
                //更新
                Config config = configDao.selectById(dto.getId());
                if(config != null){
                    config.setState(dto.getState());
                    config.setOperateUserId(dto.getUserId());
                    config.setOperateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                    n = configDao.updateById(config);
                }
                if(n > 0){
                    return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg());
                }
            }
        }catch (Exception e){
            log.info("更新系统配置出现异常");
            throw new CommonException(e.getMessage());
        }
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }
}
