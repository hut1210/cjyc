package com.cjyc.customer.api.service;

import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/14 9:35
 *  @Description:处理系统文件
 */
public interface ISystemFileService {

    /**
     * 获取移动端用户所有banner图片
     * @return
     */
    List<String> getAllBannerPhoto();
}
