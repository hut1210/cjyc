package com.cjyc.common.system.service;

import com.cjyc.common.model.enums.SendNoTypeEnum;

public interface ISendNoService {
	/**
	 * 发号
	 * @author JPG
	 * @since 2019/10/17 11:50
	 * @param type
	 */
	public String getNo(SendNoTypeEnum type, int noLength);

	public String getNo(SendNoTypeEnum type);


	/**
	 * 发订单编号
	 * @author JPG
	 * @since 2019/11/20 9:20
	 * @param
	 */
	public String getOrderNo();

}
