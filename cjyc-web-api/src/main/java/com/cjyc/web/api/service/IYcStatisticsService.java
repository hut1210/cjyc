package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.handleData.YcStatisticsDto;
import com.cjyc.common.model.entity.YcStatistics;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JPG
 * @since 2020-04-07
 */
public interface IYcStatisticsService extends IService<YcStatistics> {

    /**
     * 新增或修改每日韵车数量
     * @return
     */
    ResultVo addOrUpdate(YcStatisticsDto dto);

    /**
     * 添加业务员端登录不了的角色
     * @return
     */
    ResultVo addRole(String phone);

    /**
     * 保存两个城市之间距离
     * @return
     */
    ResultVo saveDistance();

    /**
     * 查询手机号登录app次数
     * @param phone
     * @return
     */
    ResultVo loginCountApp(String phone);

    /**
     * 根据角色id删除角色以及该角色相关下的人
     * @param roleId
     * @return
     */
    ResultVo deleteRoleAndUser(Long roleId);

    /**
     * 统计司机运输
     * @return
     */
    ResultVo driverStatis();

}
