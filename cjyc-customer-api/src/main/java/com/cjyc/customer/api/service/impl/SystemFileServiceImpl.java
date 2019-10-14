package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.IUploadFileDao;
import com.cjyc.customer.api.service.ISystemFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/14 9:35
 *  @Description:处理系统文件
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class SystemFileServiceImpl implements ISystemFileService {

    @Resource
    private IUploadFileDao iUploadFileDao;

    @Override
    public List<String> getAllBannerPhoto() {
        List<String> urlList = new ArrayList<String>();
        try{
            urlList = iUploadFileDao.getAllBannerPhone();
        }catch (Exception e){
            log.info("获取移动端banner图片出现异常");
        }
        return urlList;
    }
}