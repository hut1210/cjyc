package com.cjyc.web.api.service.impl;

import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.constant.PatternConstant;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.web.api.service.ISendNoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

/**
 * 发号
 * @author JPG
 */
@Service
@Slf4j
public class SendNoServiceImpl implements ISendNoService {

	@Autowired
	private StringRedisUtil redisUtil;
	private static final DecimalFormat FOUR_FORMAT = new DecimalFormat("0000");
	private static final DecimalFormat FIVE_FORMAT = new DecimalFormat("00000");
	private static final DecimalFormat SIX_FORMAT = new DecimalFormat("000000");
	private static final DecimalFormat SEVEN_FORMAT = new DecimalFormat("0000000");


	/**
	 * 发无随机数号
	 *
	 * @param type
	 * @author JPG
	 * @since 2019/10/17 11:50
	 */
	@Override
	public String getNo(SendNoTypeEnum type, int noLength) {
		return getNo(type, noLength,0,0);
	}
	/**
	 * 发订单号
	 *
	 * @param type
	 * @author JPG
	 * @since 2019/10/17 11:50
	 */
	@Override
	public String getNo(SendNoTypeEnum type) {
		return getNo(type,5, 2,2);
	}

	/**
	 *
	 * @author JPG
	 * @since 2019/10/17 12:49
	 * @param sendNoType 发号类型枚举
	 * @param noLength 发号长度
	 * @param timeTailRandomLength 时间后随机数位数
	 * @param noTailRandomLength 编号后随机数位数
	 */
	public String getNo(SendNoTypeEnum sendNoType, int noLength, Integer timeTailRandomLength, Integer noTailRandomLength) {
		String time = LocalDateTimeUtil.formatLDTNow(PatternConstant.SUB_SIMPLE_DATE);
		String no = "";
		try {
			String key = getSendNoKey(sendNoType.name, time);
			if (!redisUtil.exists(key)) {
				redisUtil.setExpire(key, 86400);
			}

			long incrbySendNo = redisUtil.incr(key);

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
		switch (noLength){
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


}