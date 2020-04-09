package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjkj.usercenter.dto.common.UserResp;
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
import org.springframework.transaction.annotation.Transactional;
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
    private ICarrierDao carrierDao;
    @Resource
    private ICustomerDao customerDao;
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
                    if(Double.valueOf(distance) != null){
                        BigDecimal bd = BigDecimal.valueOf(distance).setScale(0, BigDecimal.ROUND_DOWN);
                        line.setKilometer(bd);
                    }else{
                        line.setKilometer(BigDecimal.ZERO);
                    }
                    lineDao.updateById(line);
                }else{
                    continue;
                }
            }
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo loginCountApp(String phone) {
        //查询承运商下账号登录app次数
        Carrier carrier = carrierDao.selectOne(new QueryWrapper<Carrier>().lambda().eq(Carrier::getLinkmanPhone, phone));
        //查询业务员账号登录app次数
        Admin admin = adminDao.selectOne(new QueryWrapper<Admin>().lambda().eq(Admin::getPhone, phone));
        //查询用户账号登录app次数
        Customer customer = customerDao.selectOne(new QueryWrapper<Customer>().lambda().eq(Customer::getContactPhone, phone));
        if(null == carrier || admin == null || customer == null){
            return BaseResultUtil.fail("账号不存在，请检查");
        }
        ResultData<UserResp> userRd = sysUserService.getByAccount(phone);
        if(!ResultDataUtil.isSuccess(userRd)){
            return BaseResultUtil.fail("查询用户信息错误，原因：" + userRd.getMsg());
        }
        return BaseResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo deleteRoleAndUser(Long roleId) {
        //因为只是删除app_调度经理角色，慎重起见先查询
        //处理架构组那边角色
        ResultData<SelectRoleResp> roleRd = sysRoleService.getById(roleId);
        if(!ResultDataUtil.isSuccess(roleRd)){
            return BaseResultUtil.fail("获取角色失败");
        }
        if(!"APP_调度经理".equals(roleRd.getData().getRoleName())){
            return BaseResultUtil.fail("APP_调度经理角色不存在");
        }
        ResultData<SelectRoleResp> deleRd = sysRoleService.delete(roleId);
        if(!ResultDataUtil.isSuccess(deleRd)){
            return BaseResultUtil.fail("删除角色失败");
        }
        //处理韵车这边角色，查询角色信息
        Role role = roleDao.selectOne(new QueryWrapper<Role>().lambda().eq(Role::getRoleId, roleId));
        if(role == null){
            return BaseResultUtil.fail("APP_调度经理不存在");
        }
        //根据角色查询该角色下所挂用户
        List<UserRoleDept> urdList = userRoleDeptDao.selectList(new QueryWrapper<UserRoleDept>().lambda().eq(UserRoleDept::getRoleId, role.getId()));
        if(!CollectionUtils.isEmpty(urdList)){
            //循环删除用户
            urdList.forEach(urd -> {
                adminDao.deleteById(urd.getUserId());
            });
        }
        //删除用户关系数据
        userRoleDeptDao.delete(new QueryWrapper<UserRoleDept>().lambda().eq(UserRoleDept::getRoleId,role.getId()));
        //删除角色
        roleDao.delete(new QueryWrapper<Role>().lambda().eq(Role::getRoleId,roleId));
        return BaseResultUtil.success();
    }
}
