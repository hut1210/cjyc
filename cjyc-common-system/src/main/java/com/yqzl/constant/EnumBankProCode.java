package com.yqzl.constant;

/**
 * 资金系统支持的银行产品枚举
 *
 * @author RenPL 2020-3-15
 */
public enum EnumBankProCode {

    /**
     * 交通银行
     */
    COMM_BANK("CommBankClientImpl"),
    ;

    EnumBankProCode(final String beanName) {
        this.beanName = beanName;
    }

    /**
     * Spring Bean 名称
     */
    private final String beanName;

    public String getBeanName() {
        return beanName;
    }

    public static EnumBankProCode fromCode(String typeCode) {
        if (typeCode == null) {
            throw new IllegalArgumentException("不能根据 null 获取枚举值");
        }
        for (final EnumBankProCode value : values()) {
            if (value.name().toLowerCase().equals(typeCode.toLowerCase())) {
                return value;
            }
        }
        throw new IllegalArgumentException("未知的银行产品 [" + typeCode + "]");
    }
}
