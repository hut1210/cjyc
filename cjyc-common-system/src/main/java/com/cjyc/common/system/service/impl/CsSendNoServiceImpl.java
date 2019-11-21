package com.cjyc.common.system.service.impl;

import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.RandomUtil;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.common.system.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 发号
 *
 * @author JPG
 */
@Service
@Slf4j
public class CsSendNoServiceImpl implements ICsSendNoService {
    @Resource
    private RedisDistributedLock redisLock;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private ICustomerDao customerDao;
    @Autowired
    private RedisUtils redisUtil;
    private static final DecimalFormat TWO_FORMAT = new DecimalFormat("00");
    private static final DecimalFormat THREE_FORMAT = new DecimalFormat("000");
    private static final DecimalFormat FOUR_FORMAT = new DecimalFormat("0000");
    private static final DecimalFormat FIVE_FORMAT = new DecimalFormat("00000");
    private static final DecimalFormat SIX_FORMAT = new DecimalFormat("000000");
    private static final DecimalFormat SEVEN_FORMAT = new DecimalFormat("0000000");

    @Override
    public String formatNo(String prefixNo, int indexNo, int formatLength) {
        return prefixNo == null ? "" : prefixNo + "-" + getDecimalFormat(formatLength).format(indexNo);
    }

    /**
     * 发号：X8888888
     *
     * @param type
     * @author JPG
     * @since 2019/10/17 11:50
     */
    @Override
    public String getNo(SendNoTypeEnum type) {
        if (type == SendNoTypeEnum.ORDER) {
            return getNo(type, 5);
        }
        if (type == SendNoTypeEnum.WAYBILL) {
            return getNo(type, 6);
        }
        if (type == SendNoTypeEnum.CUSTOMER) {
            return getRandomNo(type, 8);
        }
        if (type == SendNoTypeEnum.DRIVER) {
            return getRandomNo(type, 8);
        }
        if (type == SendNoTypeEnum.COUPON) {
            return getNo(type, 3);
        }
        if (type == SendNoTypeEnum.PAYMENT) {
            return getNo(type, 6);
        }
        if (type == SendNoTypeEnum.RECEIPT) {
            return getNo(type, 6);
        }
        return getNo(type, 5, 2, 2);
    }

    private String getRandomNo(SendNoTypeEnum type, int randomLength) {
        String driverNo = null;
        String lockKey = getRandomNoKey(type.prefix);
        try {
            if (!redisLock.lock(lockKey, 86400, 10, 200)) {
                throw new RuntimeException("获取订单编号失败");
            }

            String setKey = getRandomNoSetKey(type.prefix);
            Set<String> set = null;

            if (redisUtil.hasKey(setKey)) {
                set = redisUtil.sMembers(setKey);
            }
            if (CollectionUtils.isEmpty(set)) {
                List<String> list = null;
                if (type == SendNoTypeEnum.DRIVER) {
                    list = driverDao.findAllNo();
                } else if (type == SendNoTypeEnum.CUSTOMER) {
                    list = customerDao.findAllNo();
                }
                if (!CollectionUtils.isEmpty(list)) {
                    set = new HashSet<>(list);
                    set.remove(null);
                    String[] strings = set.toArray(new String[0]);
                    redisUtil.sAdd(setKey, strings);
                    redisUtil.expire(setKey, 1, TimeUnit.DAYS);
                }
            }
            if (CollectionUtils.isEmpty(set)) {
                driverNo = type.prefix + RandomUtil.getMathRandom(randomLength);
            } else {
                int count = 0;
                while (driverNo == null) {
                    if (count > 100000) {
                        throw new ServerException("获取订单编号超时");
                    }
                    String temp = type.prefix + RandomUtil.getMathRandom(randomLength);
                    if (!set.contains(temp)) {
                        driverNo = temp;
                    }
                    count++;
                }
            }
            redisUtil.sAdd(setKey, driverNo);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        } finally {
            redisUtil.delete(lockKey);
        }

        return driverNo;
    }

    /**
     * 发订单编号
     * D+6位日期+5位随机数字组合
     *
     * @author JPG
     * @since 2019/11/20 9:20
     */
    private String getNo(SendNoTypeEnum sendNoTypeEnum, int randomLength) {

        String time = LocalDateTimeUtil.formatLDTNow(TimePatternConstant.SUB_SIMPLE_SIMPLE_DATE);
        String random = null;
        String lockKey = getSendNoKey(sendNoTypeEnum.prefix, time);
        try {
            if (!redisLock.lock(lockKey, 86400, 10, 200)) {
                throw new RuntimeException("获取订单编号失败");
            }

            String setKey = getSendNoSetKey(sendNoTypeEnum.prefix, time);
            if (!redisUtil.hasKey(lockKey)) {
                redisUtil.expire(lockKey, 1, TimeUnit.DAYS);
            }
            Set<String> set = redisUtil.sMembers(setKey);
            if (CollectionUtils.isEmpty(set)) {
                random = RandomUtil.getMathRandom(randomLength);
            } else {
                while (random == null) {
                    String temp = RandomUtil.getMathRandom(randomLength);
                    if (!set.contains(temp)) {
                        random = temp;
                    }
                }
            }
            redisUtil.sAdd(setKey, random);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            random = RandomUtil.getMathRandom(randomLength);
        } finally {
            redisUtil.delete(lockKey);
        }
        StringBuffer orderNo = new StringBuffer();
        orderNo.append(sendNoTypeEnum.prefix == null ? "" : sendNoTypeEnum.prefix);
        orderNo.append(time);
        orderNo.append(random);
        return orderNo.toString();
    }

    /**
     * @param sendNoType           发号类型枚举
     * @param noLength             发号长度
     * @param timeTailRandomLength 时间后随机数位数
     * @param noTailRandomLength   编号后随机数位数
     * @author JPG
     * @since 2019/10/17 12:49
     */
    public String getNo(SendNoTypeEnum sendNoType, int noLength, Integer timeTailRandomLength, Integer noTailRandomLength) {
        String time = LocalDateTimeUtil.formatLDTNow(TimePatternConstant.SUB_SIMPLE_SIMPLE_DATE);
        String no = "";
        try {
            String key = getSendNoKey(sendNoType.name, time);
            if (!redisUtil.hasKey(key)) {
                redisUtil.expire(key, 1, TimeUnit.DAYS);
            }

            long incrbySendNo = redisUtil.incrBy(key, 1);

            DecimalFormat DECIMAL_FORMAT = getDecimalFormat(noLength);

            no = DECIMAL_FORMAT.format(incrbySendNo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            no = RandomStringUtils.random(noLength, false, true);
        }
        StringBuffer sb = new StringBuffer();
        String value = sendNoType.prefix;
        if (StringUtils.isNotBlank(value)) {
            sb.append(value);
        }
        sb.append(time);
        sb.append(RandomStringUtils.randomNumeric(timeTailRandomLength));
        sb.append(no);
        sb.append(RandomStringUtils.randomNumeric(noTailRandomLength));
        return sb.toString();
    }

    private DecimalFormat getDecimalFormat(Integer noLength) {
        DecimalFormat decimalFormat;
        switch (noLength) {
            case 2:
                decimalFormat = TWO_FORMAT;
                break;
            case 3:
                decimalFormat = THREE_FORMAT;
                break;
            case 4:
                decimalFormat = FOUR_FORMAT;
                break;
            case 5:
                decimalFormat = FIVE_FORMAT;
                break;
            case 6:
                decimalFormat = SIX_FORMAT;
                break;
            default:
                decimalFormat = SEVEN_FORMAT;
        }
        return decimalFormat;

    }

    private String getSendNoKey(String type, String time) {
        return "cjyc:send:no:" + type + ":" + time;
    }

    private String getSendNoSetKey(String prefix, String time) {
        return "cjyc:send:no:set:" + prefix + ":" + time;
    }

    private String getRandomNoKey(String prefix) {
        return "cjyc:random:no:" + prefix;
    }

    private String getRandomNoSetKey(String prefix) {
        return "cjyc:random:no:set:" + prefix;
    }

}