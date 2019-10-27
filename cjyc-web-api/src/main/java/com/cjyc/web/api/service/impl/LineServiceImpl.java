package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dao.ILineNodeDao;
import com.cjyc.common.model.dto.web.inquiry.SelectInquiryDto;
import com.cjyc.common.model.dto.web.line.AddAndUpdateLineDto;
import com.cjyc.common.model.dto.web.line.SortNodeDto;
import com.cjyc.common.model.dto.web.line.SortNodeListDto;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.ProvinceCityVo;
import com.cjyc.common.model.vo.web.line.LineVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.ILineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class LineServiceImpl extends ServiceImpl<ILineDao, Line> implements ILineService {

    @Resource
    private ILineNodeDao lineNodeDao;

    @Resource
    private ILineDao lineDao;

    @Resource
    private ICityDao iCityDao;

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

    @Override
    public ResultVo<PageVo<LineVo>> getLineByTerm(SelectInquiryDto dto) {
        try{
            PageInfo<LineVo> pageInfo = null;
            List<LineVo> lineVos = lineDao.getLineByTerm(dto);
            if(lineVos != null && lineVos.size() > 0){
                for(LineVo vo : lineVos){
                    //获取起始省市
                    if(StringUtils.isNotBlank(vo.getStartCityCode())){
                        ProvinceCityVo pcvo = iCityDao.getProvinceCityByCode(vo.getStartCityCode());
                        if(pcvo != null){
                            vo.setStartProvinceCode(pcvo.getProvinceCode());
                            vo.setStartProvince(pcvo.getProvinceName());
                            vo.setStartCityCode(pcvo.getProvinceCode());
                            vo.setStartCity(pcvo.getCityName());
                        }
                    }
                    //获取目的省市
                    if(StringUtils.isNotBlank(vo.getEndCityCode())){
                        ProvinceCityVo pcvo = iCityDao.getProvinceCityByCode(vo.getEndCityCode());
                        if(pcvo != null){
                            vo.setEndProvinceCode(pcvo.getProvinceCode());
                            vo.setEndProvince(pcvo.getProvinceName());
                            vo.setEndCityCode(pcvo.getProvinceCode());
                            vo.setEndCity(pcvo.getCityCode());
                        }
                    }
                    vo.setDefaultWlFee(vo.getDefaultWlFee() == null ? BigDecimal.ZERO : vo.getDefaultWlFee().divide(new BigDecimal(100)));
                    vo.setDefaultFreightFee(vo.getDefaultFreightFee() == null ? BigDecimal.ZERO:vo.getDefaultFreightFee().divide(new BigDecimal(100)));
                    if(StringUtils.isNotBlank(vo.getCreateTime())){
                        long ldt = LocalDateTimeUtil.convertToLong(vo.getCreateTime(), TimePatternConstant.COMPLEX_TIME_FORMAT);
                        vo.setCreateTime(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(ldt),TimePatternConstant.COMPLEX_TIME_FORMAT));
                    }
                    PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                    pageInfo = new PageInfo<>(lineVos);
                    return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
                }
            }
        }catch (Exception e){
            log.info("根据条件查询班线出现异常");
            throw new CommonException(e.getMessage());
        }
        return null;
    }

    @Override
    public ResultVo addAndUpdateLine(AddAndUpdateLineDto dto) {
        int no = 0;
        Line line ;
        try{
            //新增
            if(dto.getFlag() == FlagEnum.ADD.code){
                line = new Line();
                line = encapLine(line,dto);
                line.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                line.setCreateUserId(dto.getUserId());
                no = lineDao.insert(line);
            }else if(dto.getFlag() == FlagEnum.DUPDTATE.code){
                //更新
                line = lineDao.selectById(dto.getId());
                if(line != null){
                    line = encapLine(line,dto);
                    line.setUpdateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                    line.setUpdateUserId(dto.getUserId());
                    no = lineDao.updateById(line);
                }
            }
            if(no > 0){
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg());
            }
        }catch (Exception e){
            log.info("新增/更新班线出现异常");
            throw new CommonException(e.getMessage());
        }
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @Override
    public ResultVo deleteLineByIds(List<Long> lineIds) {
        int no ;
        try{
            no = lineDao.deleteBatchIds(lineIds);
            if(no > 0){
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg());
            }
        }catch (Exception e){
            log.info("批量删除班线出现异常");
            throw new CommonException(e.getMessage());
        }
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    /**
     * 封装班线line
     * @param line
     * @return
     */
    private Line encapLine(Line line,AddAndUpdateLineDto dto){
        line.setFromCode(dto.getStartCityCode());
        line.setToCode(dto.getEndCityCode());
        line.setKilometer(dto.getKilometer());
        line.setDays(dto.getDays());
        line.setDefaultWlFee(dto.getDefaultWlFee() == null ? BigDecimal.ZERO:dto.getDefaultWlFee().divide(new BigDecimal(100)));
        line.setDefaultFreightFee(dto.getDefaultFreightFee() == null ? BigDecimal.ZERO:dto.getDefaultFreightFee().divide(new BigDecimal(100)));
        line.setRemark(dto.getRemark());
        line.setName(dto.getStartCity()+ NoConstant.SEPARATOR+dto.getEndCity());
        line.setCode(dto.getStartCityCode()+dto.getEndCityCode());
        return line;
    }
}
