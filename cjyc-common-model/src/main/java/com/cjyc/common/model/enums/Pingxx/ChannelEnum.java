package com.cjyc.common.model.enums.Pingxx;

public enum ChannelEnum {
    /***/
    ALIPAY("支付宝 APP 支付", "alipay"),
    ALIPAY_WAP("支付宝手机网站支付", "alipay_wap"),
    ALIPAY_QR("支付宝扫码支付", "alipay_qr"),
    ALIPAY_SCAN("支付宝条码支付", "alipay_scan"),
    ALIPAY_LITE("支付宝小程序支付", "alipay_lite"),
    ALIPAY_PC_DIRECT("支付宝电脑网站支付", "alipay_pc_direct"),
    WX("微信 APP 支付", "wx"),
    WX_PUB("微信 JSAPI 支付", "wx_pub"),
    WX_PUB_QR("微信 NATIVE 支付", "wx_pub_qr"),
    WX_PUB_SCAN("微信付款码支付", "wx_pub_scan"),
    WX_WAP("微信 H5 支付", "wx_wap"),
    WX_LITE("微信小程序支付", "wx_lite"),
    QPAY("QQ 钱包 APP 支付", "qpay"),
    QPAY_PUB("QQ 钱包公众号支付", "qpay_pub"),
    UPACP("银联手机控件支付（银联 APP 支付）", "upacp"),
    UPACP_PC("银联网关支付（银联 PC 网页支付）", "upacp_pc"),
    UPACP_QR("银联二维码（主扫）", "upacp_qr"),
    UPACP_SCAN("银联二维码（被扫）", "upacp_scan"),
    UPACP_WAP("银联手机网站支付", "upacp_wap"),
    UPACP_B2B("银联企业网银支付（银联 B2B PC 网页支付）", "upacp_b2b"),
    CP_B2B("银联电子企业网银支付（银联电子 B2B PC 网页支付）", "cp_b2b"),
    APPLEPAY_UPACP("APPLE PAY", "applepay_upacp"),
    CMB_WALLET("招行一网通", "cmb_wallet"),
    CMB_PC_QR("招行 PC 扫码支付", "cmb_pc_qr"),
    BFB_WAP("百度钱包", "bfb_wap"),
    JDPAY_WAP("京东支付", "jdpay_wap"),
    YEEPAY_WAP("易宝支付", "yeepay_wap"),
    ISV_QR("线下扫码（主扫）", "isv_qr"),
    ISV_SCAN("线下扫码（被扫）", "isv_scan"),
    ISV_WAP("线下扫码（固定码）", "isv_wap"),
    ISV_LITE("线下小程序支付", "isv_lite"),
    CCB_PAY("建行 APP 支付", "ccb_pay"),
    CCB_QR("建行二维码支付", "ccb_qr"),
    CMPAY("移动和包支付", "cmpay"),
    COOLCREDIT("库分期", "coolcredit"),
    CB_ALIPAY("跨境支付宝 APP 支付", "cb_alipay"),
    CB_ALIPAY_WAP("跨境支付宝手机网站支付", "cb_alipay_wap"),
    CB_ALIPAY_QR("跨境支付宝扫码支付", "cb_alipay_qr"),
    CB_ALIPAY_SCAN("跨境支付宝条码支付", "cb_alipay_scan"),
    CB_ALIPAY_PC_DIRECT("跨境支付宝电脑网站支付", "cb_alipay_pc_direct"),
    CB_WX("跨境微信 APP 支付", "cb_wx"),
    CB_WX_PUB("跨境微信 JSAPI 支付", "cb_wx_pub"),
    CB_WX_PUB_QR("跨境微信 NATIVE 支付", "cb_wx_pub_qr"),
    CB_WX_PUB_SCAN("跨境微信付款码支付", "cb_wx_pub_scan"),
    PAYPAL("PAYPAL", "paypal"),
    BALANCE("余额", "balance"),
    ALLINPAY("通联代付","allinpay");

    private String name;
    private String tag;

    ChannelEnum(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public static ChannelEnum valueOfTag(String tag) {
        for (ChannelEnum codeEnum : values()) {
            if (codeEnum.tag.equals(tag)) {
                return codeEnum;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + tag + "]");
    }
}
