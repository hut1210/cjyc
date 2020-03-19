package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.postal.PostalDto;
import com.cjyc.common.model.entity.ChinaPostalCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;
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
public interface IChinaPostalCodeService extends IService<ChinaPostalCode> {

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

}
