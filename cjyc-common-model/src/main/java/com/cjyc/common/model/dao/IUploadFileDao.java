package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.UploadFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
public interface IUploadFileDao extends BaseMapper<UploadFile> {

    /**
     * 获取移动端所有banner图片地址
     * @return
     */
    List<String> getAllBannerPhone();

}
