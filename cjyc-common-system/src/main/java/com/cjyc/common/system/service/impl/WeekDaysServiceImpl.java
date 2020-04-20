package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.IWeekDaysDao;
import com.cjyc.common.model.dto.WeekDaysDto;
import com.cjyc.common.model.entity.WeekDays;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.HttpClientUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.IWeekDaysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Package: com.cjyc.web.api.service.impl
 * @Description: 统计工作日
 * @Author: Yang.yanfei
 * @Date: 2020/4/15
 * @Version: V1.0
 * @Copyright: 2019 - 2020 - ©长久科技
 */
@Slf4j
@Service
public class WeekDaysServiceImpl implements IWeekDaysService {
    @Resource
    private IWeekDaysDao weekDaysDao;

    @Override
    public void updateDateSun(int yearNum) {
        String startDate = LocalDate.now().plusYears(yearNum).getYear() + "0101";
        String endDate = LocalDate.now().plusYears(yearNum).getYear() + "1231";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate1 = LocalDate.parse(startDate, formatter);
        LocalDate endDate1 = LocalDate.parse(endDate, formatter);
        long daysBetween = ChronoUnit.DAYS.between(startDate1, endDate1);
        for (int i = 0; i <= daysBetween; i++) {
            if (startDate1.plusDays(i).getDayOfWeek().getValue() == Calendar.JULY || startDate1.plusDays(i).getDayOfWeek().getValue() == Calendar.AUGUST) {
                WeekDays weekDays = this.getWeekDays(formatter.format(startDate1.plusDays(i)));
                weekDays.setType(1);
                weekDaysDao.updateById(weekDays);
            }
        }
    }

    @Override
    public void insertWeekDaysInfo(int year) {
        // 请注意月份是从0-11,天数是1， 2020-1-1 至 2020-12-31
        Calendar start = Calendar.getInstance();
        // 2020-1-1 开始
        start.set(year, Calendar.JANUARY, Calendar.FEBRUARY);
        Calendar end = Calendar.getInstance();
        // 2021--0-0结束，2021-1-1不算
        end.set(year + Calendar.FEBRUARY, Calendar.JANUARY, Calendar.JANUARY);
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        while (start.compareTo(end) <= Calendar.JANUARY) {
            String str = format.format(start.getTime());
            // 查询是否已经存在这个日期了
            WeekDays weekDays = getWeekDays(str);
            if (weekDays == null) {
                weekDays = new WeekDays() {{
                    setDate(str);
                }};
                weekDaysDao.insert(weekDays);
            }
            // 循环，每次天数加1
            start.set(Calendar.DATE, start.get(Calendar.DATE) + Calendar.FEBRUARY);
        }
    }

    @Override
    public void updateWeekDaysTypeInfo(int year) {
        // 拼接请求参数
        String dates = getYearDate(year);
        // 调用百度获取年月节假日的接口
        String url = "http://www.easybots.cn/api/holiday.php";
        // 请求接口
        Map<String, String> map = new HashMap<>(1);
        map.put("m", dates);
        // 返回信息
        String reu = HttpClientUtil.doGet(url, map);
        log.info("调用百度接口返回的节假日信息：" + reu);
        JSONObject jsonObject = JSONObject.parseObject(reu);
        // 解析返回信息
        final int end = 13;
        String dateYear;
        for (int i = 1; i < end; i++) {
            if (i <= 9) {
                dateYear = year + "0" + i;
            } else {
                dateYear = year + "" + i;
            }
            // 把当年当月的节假日转成map
            HashMap<String, Object> nwMap = JSON.parseObject(jsonObject.get(dateYear).toString(), new TypeReference<HashMap<String, Object>>() {
            });
            final int endDate = 32;
            String date;
            for (int j = 1; j < endDate; j++) {
                if (j <= 9) {
                    date = "0" + j;
                } else {
                    date = String.valueOf(j);
                }
                if (nwMap.get(date) != null) {
                    WeekDays weekDays = getWeekDays(dateYear + date);
                    weekDays.setType(1);
                    weekDaysDao.updateById(weekDays);
                }
            }
        }
    }

    @Override
    public ResultVo<Integer> getDateNum(String startDate, String endDate) {
        QueryWrapper<WeekDays> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ge(WeekDays::getDate, startDate).le(WeekDays::getDate, endDate).eq(WeekDays::getType, 0);
        return BaseResultUtil.success(weekDaysDao.selectCount(queryWrapper));
    }

    @Override
    public ResultVo<WeekDaysDto> getWeekDaysTypeInfo(String date) {
        QueryWrapper<WeekDays> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WeekDays::getDate, date);
        return Optional.ofNullable(weekDaysDao.selectOne(queryWrapper))
                .map(WeekDaysServiceImpl::fromEntityToDto)
                .map(BaseResultUtil::success)
                .orElse(BaseResultUtil.fail());
    }

    private static WeekDaysDto fromEntityToDto(WeekDays entity) {
        final WeekDaysDto dto = new WeekDaysDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    /**
     * 获取当前年的所有天数
     *
     * @return
     */
    private static String getYearDate(int year) {
        StringBuilder stringBuffer = new StringBuilder();
        final int end = 13;
        for (int i = 1; i < end; i++) {
            if (i <= 9) {
                stringBuffer.append(year).append("0").append(i).append(",");
            } else {
                stringBuffer.append(year).append(i);
                if (i < 12) {
                    stringBuffer.append(",");
                }
            }
        }
        log.info("请求参数：" + stringBuffer.toString());
        return stringBuffer.toString();
    }

    /**
     * 根据时间查询时间类型
     *
     * @param date
     * @return
     */
    private WeekDays getWeekDays(String date) {
        QueryWrapper<WeekDays> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WeekDays::getDate, date);
        return weekDaysDao.selectOne(queryWrapper);
    }
}
