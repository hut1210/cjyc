package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.config.ConfigDto;
import com.cjyc.common.model.entity.Config;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-29
 */
public interface IConfigService extends IService<Config> {

    /**
     * 询或者更新系统配置
     * @param dto
     * @return
     */
    ResultVo queryConfig(ConfigDto dto);

    /**
     * 更新系统配置
     * @param dto
     * @return
     */
    ResultVo updateConfig(ConfigDto dto);

}
