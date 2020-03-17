package com.cjyc.web.api.service.impl;

import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.dto.web.postal.PostalDto;
import com.cjyc.common.model.dto.web.postal.PostalImportExcel;
import com.cjyc.common.model.entity.ChinaPostalCode;
import com.cjyc.common.model.dao.IChinaPostalCodeDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.postal.ProvinceVo;
import com.cjyc.web.api.service.IChinaPostalCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

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
public class ChinaPostalCodeServiceImpl extends ServiceImpl<IChinaPostalCodeDao, ChinaPostalCode> implements IChinaPostalCodeService {

    @Resource
    private IChinaPostalCodeDao chinaPostalCodeDao;


    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public boolean importPostalCodeExcel(MultipartFile file, Long loginId) {
        boolean result;
        try {
            List<PostalImportExcel> postalImportList = ExcelUtil.importExcel(file, 0, 1, PostalImportExcel.class);
            if(!CollectionUtils.isEmpty(postalImportList)){
                for(PostalImportExcel postal : postalImportList){
                    ChinaPostalCode cpc = new ChinaPostalCode();
                    cpc.setProvinceName(postal.getProvinceName());
                    cpc.setAreaName(postal.getAreaName());
                    cpc.setPostalCode(postal.getPostalCode());
                    cpc.setAreaCode(postal.getAreaCode());
                    cpc.setCreateUserId(loginId);
                    cpc.setCreateTime(NOW);
                    chinaPostalCodeDao.insert(cpc);
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
    public ResultVo<List<ProvinceVo>> findChinaPostal(PostalDto dto) {
        List<ProvinceVo> postal = chinaPostalCodeDao.findPostal(dto.getKeyword());
        return BaseResultUtil.success(postal);
    }
}
