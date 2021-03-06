package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dto.AppVersionDto;
import com.cjyc.common.model.entity.AppVersion;
import com.cjyc.common.model.dao.IAppVersionDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.vo.AppVersionVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsAppVersionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JPG
 * @since 2020-03-06
 */
@Service
@Slf4j
public class CsAppVersionServiceImpl extends ServiceImpl<IAppVersionDao, AppVersion> implements ICsAppVersionService {

    @Resource
    private IAppVersionDao appVersionDao;

    @Override
    public ResultVo<AppVersionVo> updateAppVersion(AppVersionDto dto) {
        log.info("====>app版本更新,请求json数据 :: "+ JsonUtils.objectToJson(dto));
        //验证库中是否有值
        List<AppVersion> oldAppList = appVersionDao.selectList(new QueryWrapper<AppVersion>().lambda()
                                         .eq(AppVersion::getSystemType, dto.getSystemType())
                                         .eq(AppVersion::getAppType, dto.getAppType())
                                         .eq(AppVersion::getVersion, dto.getVersion())
                                         .eq(AppVersion::getIsActive, UseStateEnum.USABLE.code));
        //如果不存在则成功返回
        if(CollectionUtils.isEmpty(oldAppList)){
            return BaseResultUtil.success();
        }
        //获取库中符合条件的数据
        List<AppVersion> appVersionList = appVersionDao.selectList(new QueryWrapper<AppVersion>().lambda()
                                                        .eq(AppVersion::getSystemType, dto.getSystemType())
                                                        .eq(AppVersion::getAppType, dto.getAppType())
                                                        .eq(AppVersion::getIsActive, UseStateEnum.USABLE.code)
                                                        .orderByDesc(AppVersion::getId));
        AppVersion newApp = null;
        if(!CollectionUtils.isEmpty(appVersionList)){
            //获取id最大的appVersion
            Optional<AppVersion> appVersion = appVersionList.stream().max(Comparator.comparingLong(AppVersion ::getId));
            newApp = appVersion.get();
            //复制到实体中
            AppVersionVo vo = new AppVersionVo();
            BeanUtils.copyProperties(newApp,vo);
            //比较新的版本与请求传过来的比较
            if(newApp != oldAppList.get(0) && !newApp.getVersion().equals(dto.getVersion())){
                String message = encapMessage(newApp);
                vo.setMessage(message);
                return BaseResultUtil.success(vo);
            }else if(newApp.getVersion().equals(dto.getVersion())){
                //新版本与旧版本相同
                BeanUtils.copyProperties(oldAppList.get(0),vo);
                String message = encapMessage(oldAppList.get(0));
                vo.setMessage(message);
                vo.setIsUpdate(0);
                return BaseResultUtil.success(vo);
            }
        }
        return BaseResultUtil.success();
    }

    /**
     * 封装描述内容
     * @param app
     * @return
     */
    private String encapMessage(AppVersion app){
        StringBuffer sb = null;
        String message = app.getMessage();
        if(StringUtils.isNotBlank(message)){
            String[] messStr = message.split(";");
            sb = new StringBuffer();
            for(String str : messStr){
                sb.append(str+";\n");
            }
            app.setMessage(sb.toString());
        }
        return sb.toString();
    }
}
