package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.WeekDaysDto;
import com.cjyc.common.model.vo.ResultVo;

/**
 * @Package: com.cjyc.web.api.service
 * @Description:
 * @Author: Yang.yanfei
 * @Date: 2020/4/15
 * @Version: V1.0
 * @Copyright: 2019 - 2020 - ©长久科技
 */
public interface IWeekDaysService {
    /**
     * 更新当前年+n的周六日
     * @param yearNum
     */
    void updateDateSun(int yearNum);

    /**
     * 插入一年的日期
     *
     * @param year
     */
    void insertWeekDaysInfo(int year);

    /**
     * 查询节假日
     *
     * @param year
     */
    void updateWeekDaysTypeInfo(int year);

    /**
     * 查询一个时间段有多少个工作日
     * @param startDate
     * @param endDate
     * @return
     */
    ResultVo<Integer> getDateNum(String startDate, String endDate);

    /**
     * 查询一个时间是否工作日
     *
     * @param date
     * @return
     */
    ResultVo<WeekDaysDto> getWeekDaysTypeInfo(String date);
}
