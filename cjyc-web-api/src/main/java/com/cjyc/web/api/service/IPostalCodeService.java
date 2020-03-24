package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.postal.PostalDto;
import com.cjyc.common.model.entity.PostalCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.postal.AreaVo;
import com.cjyc.common.model.vo.web.postal.ProvinceVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JPG
 * @since 2020-03-16
 */
public interface IPostalCodeService extends IService<PostalCode> {

    /**
     * 导入中国邮政区号
     * @param file
     * @param loginId
     * @return
     */
    boolean importPostalCodeExcel(MultipartFile file, Long loginId);

    /**
     * 根据关键字获取省/地区
     * @param dto
     * @return
     */
    ResultVo<List<ProvinceVo>> findChinaPostal(boolean isSearchCache , PostalDto dto);

    /**
     * 查询所有的省/直辖市
     * @return
     */
    ResultVo findAllProvince(boolean isSearchCache);

    /**
     * 根据省市名称查询下属区县
     * @param isSearchCache
     * @param provinceName
     * @return
     */
    ResultVo<List<AreaVo>> findSubArea(boolean isSearchCache, String provinceName);

}
