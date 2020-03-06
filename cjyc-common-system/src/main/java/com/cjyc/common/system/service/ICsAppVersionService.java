package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.AppVersionDto;
import com.cjyc.common.model.entity.AppVersion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.AppVersionVo;
import com.cjyc.common.model.vo.ResultVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JPG
 * @since 2020-03-06
 */
public interface ICsAppVersionService extends IService<AppVersion> {

    /**
     * 检查版本更新
     * @param dto
     * @return
     */
    ResultVo<AppVersionVo> updateAppVersion(AppVersionDto dto);

}
