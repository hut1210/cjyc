package com.yqzl.service.impl;

import com.yqzl.constant.EnumBankProCode;
import com.yqzl.service.FundBankClient;
import com.yqzl.service.FundBankClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 银行工厂实现类
 *
 * @author RenPL 2020-3-15
 */
@Service
@Slf4j
public class FundBankClientFactoryImpl implements FundBankClientFactory, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public FundBankClient get(final String bankProCode) {
        if (StringUtils.isEmpty(bankProCode)) {
            throw new IllegalArgumentException("bankProCode 为空");
        }
        try {
            final String beanName = EnumBankProCode.fromCode(bankProCode).getBeanName();
            return beanFactory.getBean(beanName, FundBankClient.class);
        } catch (Exception e) {
            log.warn("获取银行 bankProCode [{}] 的客户端实现类失败", bankProCode, e);
            throw new CannotFoundBankClientException(e);
        }
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
