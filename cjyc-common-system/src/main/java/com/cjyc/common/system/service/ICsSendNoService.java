package com.cjyc.common.system.service;

import com.cjyc.common.model.enums.SendNoTypeEnum;

public interface ICsSendNoService {
	/**
	 * 发主单号
	 * @author JPG
	 * @since 2019/11/20 14:45
	 * @param type
	 */
	String getNo(SendNoTypeEnum type);


	String formatNo(String prefixNo, int indexNo, int formatLength);

}
