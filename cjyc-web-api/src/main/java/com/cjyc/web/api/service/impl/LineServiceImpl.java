package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dao.ILineNodeDao;
import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.dto.web.line.*;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.enums.CityLevelEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.PositionUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.line.LineExportExcel;
import com.cjyc.common.model.vo.web.line.LineVo;
import com.cjyc.web.api.service.ILineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.cjyc.common.model.util.PositionUtil.getLngAndLat;

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
public class LineServiceImpl extends ServiceImpl<ILineDao, Line> implements ILineService {

    @Resource
    private ILineDao lineDao;

    @Resource
    private ICityDao cityDao;

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
    public ResultVo<PageVo<LineVo>> findPageLine(SelectLineDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<LineVo> lineVos = lineDao.findAllLine(dto);
        PageInfo<LineVo> pageInfo = new PageInfo<>(lineVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo saveOrModifyLine(AddOrUpdateLineDto dto) {
        Line line = null;
        //新增
        if(dto.getLineId() == null){
            line = lineDao.getLinePriceByCode(dto.getFromCode(),dto.getToCode());
            if(line != null){
                return BaseResultUtil.fail("该班线已存在");
            }
            Line addLine = new Line();
            addLine = encapLine(addLine,dto);
            addLine.setLevel(CityLevelEnum.CITY_LEVEL.getLevel());
            addLine.setCreateTime(System.currentTimeMillis());
            addLine.setCreateUserId(dto.getLoginId());
            lineDao.insert(addLine);
        }else{
            //更新
            line = lineDao.selectById(dto.getLineId());
            if(line != null){
                line = encapLine(line,dto);
                line.setUpdateTime(System.currentTimeMillis());
                line.setUpdateUserId(dto.getLoginId());
                lineDao.updateById(line);
            }
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo getDefaultWlFeeByCode(String fromCode, String toCode) {
        Line line = lineDao.getLinePriceByCode(fromCode,toCode);
        if(line == null){
            return BaseResultUtil.fail("该班线不存在");
        }
        Map<String,Object> map = new HashMap<>(10);
        map.put("defaultWlFee",line.getDefaultWlFee() == null ? BigDecimal.ZERO : line.getDefaultWlFee().divide(new BigDecimal(100)));
        map.put("lineId",line.getDefaultWlFee() == null ? null : line.getId());
        return BaseResultUtil.success(map);
    }

    @Override
    public ResultVo<List<Line>> listByTwoCity(ListLineDto paramsDto) {
        List<Line> list = lineDao.findListByCity(paramsDto.getStartCityCode(), paramsDto.getEndCityCode());
        if(CollectionUtils.isEmpty(list)){
            return BaseResultUtil.getVo(ResultEnum.SUCCESS_NONE.getCode(),"无线路");
        }
        return BaseResultUtil.success(list);
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 获取参数
        SelectLineDto dto = getSelectLineDto(request);
        //PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        // 查询列表
        List<LineVo> lineVos = lineDao.findAllLine(dto);
        //if (!CollectionUtils.isEmpty(lineVos)) {
        // 生成导出数据
        List<LineExportExcel> exportExcelList = new ArrayList<>();
        for (LineVo vo : lineVos) {
            LineExportExcel lineExportExcel = new LineExportExcel();
            BeanUtils.copyProperties(vo, lineExportExcel);
            exportExcelList.add(lineExportExcel);
        }
        String title = "运输班线";
        String sheetName = "运输班线";
        String fileName = "运输班线.xls";
        try {
            //if(!CollectionUtils.isEmpty(exportExcelList)){
            ExcelUtil.exportExcel(exportExcelList, title, sheetName, LineExportExcel.class, fileName, response);
            //}
        } catch (IOException e) {
            log.error("导出运输班线异常:{}",e);
        }
        //}
    }

    @Override
    public boolean importExcel(MultipartFile file, Long loginId) {
        boolean result;
        try {
            List<LineImportExcel> lineImportExcelList = ExcelUtil.importExcel(file, 1, 1, LineImportExcel.class);
            if (!CollectionUtils.isEmpty(lineImportExcelList)) {
                List<Line> list = Lists.newArrayList();
                for (LineImportExcel lineExcel : lineImportExcelList) {
                    //根据城市名称查询城市code
                    City fromCity = cityDao.getCodeByName(lineExcel.getFromCity());
                    City toCity = cityDao.getCodeByName(lineExcel.getToCity());
                    if(fromCity != null && toCity != null){
                        Line line = lineDao.getLinePriceByCode(fromCity.getCode(),toCity.getCode());
                        if(line != null){
                            continue;
                        }
                    }
                    Line line = new Line();
                    BeanUtils.copyProperties(lineExcel,line);
                    line.setFromProvince(fromCity.getParentName());
                    line.setFromProvinceCode(fromCity.getParentCode());
                    line.setFromCity(fromCity.getName());
                    line.setFromCode(fromCity.getCode());

                    line.setToProvince(toCity.getParentName());
                    line.setToProvinceCode(toCity.getParentCode());
                    line.setToCode(toCity.getCode());
                    line.setToCity(toCity.getName());
                    line.setCode(fromCity.getCode()+toCity.getCode());
                    line.setDefaultWlFee(lineExcel.getDefaultWlFee() == null ? BigDecimal.ZERO:lineExcel.getDefaultWlFee().multiply(new BigDecimal(100)));
                    line.setDefaultFreightFee(lineExcel.getDefaultFreightFee() == null ? BigDecimal.ZERO:lineExcel.getDefaultFreightFee().multiply(new BigDecimal(100)));

                    String fromCityLocation = PositionUtil.getLngAndLat(lineExcel.getFromCity());
                    String toCityLocation = PositionUtil.getLngAndLat(lineExcel.getToCity());
                    double distance = PositionUtil.getDistance(Double.valueOf(fromCityLocation.split(",")[0]), Double.valueOf(fromCityLocation.split(",")[1]), Double.valueOf(toCityLocation.split(",")[0]), Double.valueOf(toCityLocation.split(",")[1]));
                    BigDecimal bd = new BigDecimal(distance).setScale(0, BigDecimal.ROUND_DOWN);
                    line.setKilometer(bd);

                    line.setDays(BigDecimal.valueOf(lineExcel.getDays()));
                    line.setName(lineExcel.getFromCity()+NoConstant.SEPARATOR+lineExcel.getToCity());
                    line.setCreateTime(System.currentTimeMillis());
                    line.setCreateUserId(loginId);
                    list.add(line);
                }
                result = super.saveBatch(list);
            } else {
                result = false;
            }
        } catch (Exception e) {
            log.error("导入班线失败异常:{}",e);
            result = false;
        }
        return result;
    }

    private SelectLineDto getSelectLineDto(HttpServletRequest request) {
        SelectLineDto dto = new SelectLineDto();
        dto.setFromCityCode(request.getParameter("fromCode"));
        dto.setToCityCode(request.getParameter("toCode"));
        dto.setLineCode(request.getParameter("code"));
        return dto;
    }
    /**
     * 封装班线line
     * @param line
     * @return
     */
    private Line encapLine(Line line, AddOrUpdateLineDto dto){
        BeanUtils.copyProperties(dto,line);
        City fromCity = cityDao.findCityByCode(dto.getFromCode());
        line.setFromProvince(fromCity.getParentName());
        line.setFromProvinceCode(fromCity.getParentCode());
        City toCity = cityDao.findCityByCode(dto.getToCode());
        line.setToProvince(toCity.getParentName());
        line.setToProvinceCode(toCity.getParentCode());
        line.setDefaultWlFee(dto.getDefaultWlFee() == null ? BigDecimal.ZERO:dto.getDefaultWlFee().multiply(new BigDecimal(100)));
        line.setDefaultFreightFee(dto.getDefaultFreightFee() == null ? BigDecimal.ZERO:dto.getDefaultFreightFee().multiply(new BigDecimal(100)));
        line.setName(dto.getFromCity()+ NoConstant.SEPARATOR+dto.getToCity());
        line.setCode(dto.getFromCode()+dto.getToCode());
        return line;
    }
}
