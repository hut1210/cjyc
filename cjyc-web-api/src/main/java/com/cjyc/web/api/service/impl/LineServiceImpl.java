package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dao.ILineNodeDao;
import com.cjyc.common.model.dto.web.line.SortNodeDto;
import com.cjyc.common.model.dto.web.line.SortNodeListDto;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ILineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 班线管理 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-18
 */
@Service
public class LineServiceImpl extends ServiceImpl<ILineDao, Line> implements ILineService {

    @Resource
    private ILineNodeDao lineNodeDao;

    @Override
    public ResultVo<String> sortNode(SortNodeListDto paramsDto) {
        @NotNull List<SortNodeDto> list = paramsDto.getList();

        Set<String> set = new HashSet<>();
        for (SortNodeDto sortNodeDto : list) {
            if(sortNodeDto == null){
                continue;
            }
            if(StringUtils.isNotBlank(sortNodeDto.getStartCityName())){
                set.add(sortNodeDto.getStartCityName());
            }
            if(StringUtils.isNotBlank(sortNodeDto.getEndCityName())){
                set.add(sortNodeDto.getEndCityName());
            }
        }

        if(set.size() > 0){
            //String lineNodeDao.findNodeSort(set);
        }

        return null;
    }


}
