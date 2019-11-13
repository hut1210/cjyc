package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dao.ILineNodeDao;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesImportExcel;
import com.cjyc.common.model.dto.web.line.*;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.enums.CityLevelEnum;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.StringUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.ProvinceCityVo;
import com.cjyc.common.model.vo.web.line.LineExportExcel;
import com.cjyc.common.model.vo.web.line.LineVo;
import com.cjyc.web.api.service.ILineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    private ILineNodeDao lineNodeDao;

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
        List<LineVo> lineVos = queryAllByTerm(dto);
        PageInfo<LineVo> pageInfo = new PageInfo<>(lineVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo addAndUpdateLine(AddOrUpdateLineDto dto) {
        Line line = null;
        //新增
        if(dto.getFlag() == FlagEnum.ADD.code){
            line = lineDao.getLinePriceByCode(dto.getFromCode(),dto.getToCode());
            if(line != null){
                return BaseResultUtil.fail("该班线已存在");
            }
            Line addLine = new Line();
            addLine = encapLine(addLine,dto);
            addLine.setLevel(CityLevelEnum.CITY_LEVEL.getLevel());
            addLine.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
            addLine.setCreateUserId(dto.getLoginId());
            lineDao.insert(addLine);
        }else if(dto.getFlag() == FlagEnum.UPDTATE.code){
            //更新
            line = lineDao.selectById(dto.getId());
            if(line != null){
                line = encapLine(line,dto);
                line.setUpdateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                line.setUpdateUserId(dto.getLoginId());
                lineDao.updateById(line);
            }
        }
      return BaseResultUtil.success();
 }

    @Override
    public ResultVo getDefaultWlFeeByCode(String fromCode, String toCode) {
        Line line = lineDao.getLinePriceByCode(fromCode,toCode);
        return BaseResultUtil.success(line.getDefaultWlFee() == null ? BigDecimal.ZERO:line.getDefaultWlFee().divide(new BigDecimal(100)));
    }

    @Override
    public ResultVo<List<Line>> listByTwoCity(ListLineDto paramsDto) {
        List<Line> list = lineDao.findListByCity(paramsDto.getStartCityCode(), paramsDto.getStartCityCode());
        return BaseResultUtil.success(list);
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 获取参数
        SelectLineDto dto = getSelectLineDto(request);
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        // 查询列表
        List<LineVo> lineVos = queryAllByTerm(dto);
        if (!CollectionUtils.isEmpty(lineVos)) {
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
                if(!CollectionUtils.isEmpty(exportExcelList)){
                    ExcelUtil.exportExcel(exportExcelList, title, sheetName, LineExportExcel.class, fileName, response);
                }
            } catch (IOException e) {
                log.error("导出运输班线异常:{}",e);
            }
        }
    }

    @Override
    public boolean importExcel(MultipartFile file, Long userId) {
        boolean result;
        try {
            List<LineImportExcel> lineImportExcelList = ExcelUtil.importExcel(file, 1, 1, LineImportExcel.class);
            if (!CollectionUtils.isEmpty(lineImportExcelList)) {
                List<Line> list = new ArrayList<>(10);
                for (LineImportExcel lineExcel : lineImportExcelList) {
                    //根据城市名称查询城市code
                    String fromCode = cityDao.getCodeByName(lineExcel.getFromCity());
                    String toCode = cityDao.getCodeByName(lineExcel.getToCity());
                    if(StringUtils.isNotBlank(fromCode) && StringUtils.isNotBlank(toCode)){
                        Line line = lineDao.getLinePriceByCode(fromCode,toCode);
                        if(line != null){
                            continue;
                        }
                    }
                    Line line = new Line();
                    BeanUtils.copyProperties(lineExcel,line);
                    line.setFromCode(fromCode);
                    line.setToCode(toCode);
                    line.setCode(fromCode+toCode);
                    line.setDefaultWlFee(lineExcel.getDefaultWlFee() == null ? BigDecimal.ZERO:lineExcel.getDefaultWlFee().multiply(new BigDecimal(100)));
                    line.setDefaultFreightFee(lineExcel.getDefaultFreightFee() == null ? BigDecimal.ZERO:lineExcel.getDefaultFreightFee().multiply(new BigDecimal(100)));
                    line.setName(lineExcel.getFromCity()+NoConstant.SEPARATOR+lineExcel.getToCity());
                    line.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                    line.setCreateUserId(userId);
                    list.add(line);
                }
                result = super.saveBatch(list);
            } else {
                result = false;
            }
        } catch (Exception e) {
            log.error("导入品牌车系失败异常:{}",e);
            result = false;
        }
        return result;
    }

    private SelectLineDto getSelectLineDto(HttpServletRequest request) {
        SelectLineDto dto = new SelectLineDto();
        dto.setCurrentPage(Integer.valueOf(request.getParameter("currentPage")));
        dto.setPageSize(Integer.valueOf(request.getParameter("pageSize")));
        dto.setFromCityCode(request.getParameter("fromCityCode"));
        dto.setToCityCode(request.getParameter("toCityCode"));
        dto.setLineCode(request.getParameter("lineCode"));
        return dto;
    }

    /**
     * 根据条件查询班线
     * @param dto
     * @return
     */
    private List<LineVo> queryAllByTerm(SelectLineDto dto){
        List<LineVo> lineVos = lineDao.getLineByTerm(dto);
        if(!CollectionUtils.isEmpty(lineVos)){
            for(LineVo vo : lineVos){
                //获取起始省市
                if(StringUtils.isNotBlank(vo.getFromCityCode())){
                    ProvinceCityVo pcvo = cityDao.getProvinceCityByCode(vo.getFromCityCode());
                    if(pcvo != null){
                        vo.setFromProvinceCode(pcvo.getProvinceCode());
                        vo.setFromProvince(pcvo.getProvinceName());
                        vo.setFromCityCode(pcvo.getCityCode());
                        vo.setFromCity(pcvo.getCityName());
                    }
                }
                //获取目的省市
                if(StringUtils.isNotBlank(vo.getToCityCode())){
                    ProvinceCityVo pcvo = cityDao.getProvinceCityByCode(vo.getToCityCode());
                    if(pcvo != null){
                        vo.setToProvinceCode(pcvo.getProvinceCode());
                        vo.setToProvinceCode(pcvo.getProvinceName());
                        vo.setToCity(pcvo.getCityName());
                        vo.setToCityCode(pcvo.getCityCode());
                    }
                }
                vo.setDefaultWlFee(vo.getDefaultWlFee() == null ? BigDecimal.ZERO : vo.getDefaultWlFee().divide(new BigDecimal(100)));
                vo.setDefaultFreightFee(vo.getDefaultFreightFee() == null ? BigDecimal.ZERO:vo.getDefaultFreightFee().divide(new BigDecimal(100)));
                if(StringUtils.isNotBlank(vo.getCreateTime())){
                    vo.setCreateTime(LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(Long.valueOf(vo.getCreateTime())),TimePatternConstant.COMPLEX_TIME_FORMAT));
                }
            }
        }
        return lineVos;
    }
    /**
     * 封装班线line
     * @param line
     * @return
     */
    private Line encapLine(Line line, AddOrUpdateLineDto dto){
        BeanUtils.copyProperties(dto,line);
        line.setDefaultWlFee(dto.getDefaultWlFee() == null ? BigDecimal.ZERO:dto.getDefaultWlFee().multiply(new BigDecimal(100)));
        line.setDefaultFreightFee(dto.getDefaultFreightFee() == null ? BigDecimal.ZERO:dto.getDefaultFreightFee().multiply(new BigDecimal(100)));
        line.setName(dto.getFromCity()+ NoConstant.SEPARATOR+dto.getToCity());
        line.setCode(dto.getFromCode()+dto.getToCode());
        return line;
    }
}
