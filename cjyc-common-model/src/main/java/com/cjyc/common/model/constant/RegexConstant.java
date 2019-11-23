package com.cjyc.common.model.constant;

/**
 * 正则常量，
 * @author JPG
 */
public class RegexConstant {

    /**
     * 正则：手机号（简单）
     * @author JPG
     */
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";

    /**
     * 正则：手机号（精确）
     * @author JPG
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    /**
     * 正则：手机号（精确,包含新增手机号段）
     * @author JPG
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <P>移动新增：172、198</P>
     * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
     * <P>联通新增：166、171</P>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <P>电信新增：149、199</P>     *
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT_LATEST = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$";

    /**
     * 正则：电话号码
     */
    public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    /**
     * 正则：身份证号码18位
     * @author JPG
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 正则：汉字
     */
    public static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 正则：昵称，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    public static final String REGEX_USERNICK = "^[0-9A-Za-z_\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    /**
     * 正则：用户名，取值范围为a-z,A-Z,0-9,"_"，不能以"_"结尾,用户名必须是6-20位
     */
    public static final String REGEX_USERNAME = "^[0-9A-Za-z_]{6,20}(?<!_)$";
    /**
     * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
     */
    public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    /**
     * 正则：IP地址
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    /**
     * 正则：验证码
     */
    public static final String VERIFY_CODE = "\\d{4}";
    /**
     * 正则：0|1
     */
    public static final String ZERO_ONE = "[0|1]";
    /**
     * 正则：车牌号
     */
    public static final String PLATE_NO = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(?:(?![A-Z]{4})[A-Z0-9]){4}[A-Z0-9挂学警港澳]{1}$";
    /**
     * 正则：姓名(包含新疆姓名)
     */
    public static final String NAME = "^[\\u4e00-\\u9fa5.·\\u36c3\\u4DAE]{0,}$";
    /**
     * 正则：银行卡号
     */
    public static final String BINK_CARD = "^([1-9]{1})(\\d{14}|\\d{18})$";

}
