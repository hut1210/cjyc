package com.cjyc.web.api.service.impl;

import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.dto.web.postal.PostalDto;
import com.cjyc.common.model.dto.web.postal.PostalImportExcel;
import com.cjyc.common.model.entity.PostalCode;
import com.cjyc.common.model.dao.IPostalCodeDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.postal.ProvinceVo;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.web.api.service.IPostalCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JPG
 * @since 2020-03-16
 */
@Service
@Slf4j
public class PostalCodeServiceImpl extends ServiceImpl<IPostalCodeDao, PostalCode> implements IPostalCodeService {

    @Resource
    private IPostalCodeDao postalCodeDao;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public boolean importPostalCodeExcel(MultipartFile file, Long loginId) {
        boolean result;
        try {
            List<PostalImportExcel> postalImportList = ExcelUtil.importExcel(file, 0, 1, PostalImportExcel.class);
            if(!CollectionUtils.isEmpty(postalImportList)){
                for(PostalImportExcel postal : postalImportList){
                    PostalCode cpc = new PostalCode();
                    cpc.setProvinceName(postal.getProvinceName());
                    cpc.setAreaName(postal.getAreaName());
                    cpc.setPostalCode(postal.getPostalCode());
                    cpc.setAreaCode(postal.getAreaCode());
                    cpc.setCreateUserId(loginId);
                    cpc.setCreateTime(System.currentTimeMillis());
                    postalCodeDao.insert(cpc);
                }
                result = true;
            } else {
                result = false;
            }
        }catch (Exception e){
            log.error("导入中国邮政区号失败异常:{}", e);
            result = false;
        }
        return result;
    }

    @Override
    public ResultVo<List<ProvinceVo>> findChinaPostal(boolean isSearchCache, PostalDto dto) {
        List<ProvinceVo> postalList = null;
        if(isSearchCache){
            //放入缓存
            String key = RedisKeys.getPostalKey(dto.getKeyword());
            String postalStr = redisUtils.hget(key,key);
            postalList = JsonUtils.jsonToList(postalStr, ProvinceVo.class);
            if(CollectionUtils.isEmpty(postalList)){
                postalList = postalCodeDao.findPostal(dto.getKeyword());
                redisUtils.hset(key, key, JsonUtils.objectToJson(postalList));
                redisUtils.expire(key, 1, TimeUnit.DAYS);
            }
        }else{
            postalList = postalCodeDao.findPostal(dto.getKeyword());
        }
        return BaseResultUtil.success(postalList);
    }
}
