package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.handleData.YcStatisticsDto;
import com.cjyc.common.model.entity.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.PositionUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.util.ResultDataUtil;
import com.cjyc.web.api.service.IYcStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JPG
 * @since 2020-04-07
 */
@Service
@Slf4j
public class YcStatisticsServiceImpl extends ServiceImpl<IYcStatisticsDao, YcStatistics> implements IYcStatisticsService {

    @Resource
    private IYcStatisticsDao ycStatisticsDao;
    @Resource
    private IAdminDao adminDao;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private IRoleDao roleDao;
    @Resource
    private ILineDao lineDao;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysUserService sysUserService;

    @Override
    public ResultVo addOrUpdate(YcStatisticsDto dto) {
        if(dto.getId() == null){
            //新增
            YcStatistics yc = new YcStatistics();
            yc.setDayCount(dto.getDayCount());
            yc.setDayTime(dto.getDayTime());
            ycStatisticsDao.insert(yc);
        }else{
            //修改
            YcStatistics yc = ycStatisticsDao.selectById(dto.getId());
            yc.setDayCount(dto.getDayCount());
            yc.setDayTime(dto.getDayTime());
            ycStatisticsDao.updateById(yc);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo addRole(String phone) {
        //根据手机号查询业务员
        Admin admin = adminDao.selectOne(new QueryWrapper<Admin>().lambda().eq(Admin::getPhone, phone));
        if(admin != null){
            List<UserRoleDept> urdList = userRoleDeptDao.selectList(new QueryWrapper<UserRoleDept>().lambda().eq(UserRoleDept::getUserId, admin.getId()));
            if(!CollectionUtils.isEmpty(urdList)){
                Role role = roleDao.selectById(urdList.get(0).getRoleId());
                if(role == null){
                    return BaseResultUtil.fail("数据错误");
                }
                Long roleId = role.getRoleId();
                Long userId = admin.getUserId();
                ResultData updateUserRd = null;
                ResultData<List<SelectRoleResp>> rolesRd = sysRoleService.getListByUserId(userId);
                if(!ResultDataUtil.isSuccess(rolesRd)){
                    return BaseResultUtil.fail("查询用户下角色列表错误，原因：" + rolesRd.getMsg());
                }
                UpdateUserReq updateUserReq = null;
                if(!CollectionUtils.isEmpty(rolesRd.getData())){
                    //存在角色
                    List<Long> roleIdList = rolesRd.getData().stream()
                            .map(r -> r.getRoleId()).collect(Collectors.toList());
                    if(roleIdList.contains(roleId)){
                        return BaseResultUtil.success();
                    }else{
                        roleIdList.add(roleId);
                        updateUserReq = new UpdateUserReq();
                        updateUserReq.setUserId(userId);
                        updateUserReq.setRoleIdList(roleIdList);
                        updateUserRd = sysUserService.update(updateUserReq);
                    }
                }else{
                    //不存在角色
                    updateUserReq = new UpdateUserReq();
                    updateUserReq.setUserId(userId);
                    updateUserReq.setRoleIdList(Arrays.asList(roleId));
                    updateUserRd = sysUserService.update(updateUserReq);
                }
                if(!ResultDataUtil.isSuccess(updateUserRd)){
                    return BaseResultUtil.fail("更新角色信息错误，原因：" + updateUserRd.getMsg());
                }
            }
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo saveDistance() {
        List<Line> lineList = lineDao.selectList(new QueryWrapper<Line>().lambda()
                                     .eq(Line::getKilometer, 0));
        if(!CollectionUtils.isEmpty(lineList)){
            for(Line line : lineList){
                if(!line.getFromCode().equals(line.getToCode())){
                    String fromCityLocation = PositionUtil.getLngAndLat(line.getFromCity());
                    String toCityLocation = PositionUtil.getLngAndLat(line.getToCity());
                    double distance = PositionUtil.getDistance(Double.valueOf(fromCityLocation.split(",")[0]), Double.valueOf(fromCityLocation.split(",")[1]), Double.valueOf(toCityLocation.split(",")[0]), Double.valueOf(toCityLocation.split(",")[1]));
                    BigDecimal bd = new BigDecimal(distance).setScale(0, BigDecimal.ROUND_DOWN);
                    line.setKilometer(bd);
                    lineDao.updateById(line);
                }else{
                    continue;
                }
            }
        }
        return BaseResultUtil.success();
    }
}
