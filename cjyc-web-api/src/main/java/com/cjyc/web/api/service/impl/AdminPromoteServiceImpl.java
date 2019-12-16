package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IAdminPromoteDao;
import com.cjyc.common.model.dto.promote.AdminPromoteQueryDto;
import com.cjyc.common.model.entity.AdminPromote;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.promote.AdminPromoteVo;
import com.cjyc.web.api.service.IAdminPromoteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务员推广表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-13
 */
@Service
public class AdminPromoteServiceImpl extends ServiceImpl<IAdminPromoteDao, AdminPromote> implements IAdminPromoteService {
    @Autowired
    private IAdminPromoteDao adminPromoteDao;

    @Override
    public ResultVo getPage(AdminPromoteQueryDto dto) {
        if (dto.getRegisterTimeEnd() != null && dto.getRegisterTimeEnd() != 0) {
            dto.setRegisterTimeEnd(TimeStampUtil.convertEndTime(dto.getRegisterTimeEnd()));
        }

        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<AdminPromoteVo> list = adminPromoteDao.selectPage(dto);
        PageInfo pageInfo = new PageInfo(list);
        return BaseResultUtil.success(pageInfo);
    }
}
