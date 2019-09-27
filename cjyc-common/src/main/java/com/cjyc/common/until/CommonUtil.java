package com.cjyc.common.until;

import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用操作工具类
 * 
 * @author leo
 * @date 2018年01月04日
 */
public class CommonUtil {

	/**
	 * 随机生成n位数字验证码
	 * @param n 随机数位数
	 * **/
	public static String randomNum(int n){
        Random rand = new Random(new Date().getTime());
        String yzm = "";
        for(int i = 0; i < n; i++){  
        	yzm += rand.nextInt(10);
        }  
        return yzm;
    }

	/**
	 * 判断传入的参数号码是否是11位合法手机号
	 * @param phone 手机号
	 */
    public static boolean valiPhoneNumber(String phone){
		//手机号正则表达式
		String regex = "^((13[0-2])|(145)|(15[5-6])|(176)|(175)|(18[5,6]))\\d{8}|(1709)\\d{7}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 判断传入的参数号码为哪家运营商
	 * @param mobile 手机号
	 * @return 运营商名称
	 */
	public static String validateMobile(String mobile){
		String returnString="";
		if(mobile==null || mobile.trim().length()!=11){
			return "未知";        //mobile参数为空或者手机号码长度不为11，错误！
		}
		String preMobile = mobile.trim().substring(0,3);
		if(preMobile.equals("134") ||  preMobile.equals("135") ||
				preMobile.equals("136") || preMobile.equals("137")
				|| preMobile.equals("138")  || preMobile.equals("139")||  preMobile.equals("147")
				||  preMobile.equals("150") || preMobile.equals("151") || preMobile.equals("152")
				|| preMobile.equals("157") || preMobile.equals("158") || preMobile.equals("159")
				|| preMobile.equals("182") || preMobile.equals("183") || preMobile.equals("184")
				|| preMobile.equals("187") || preMobile.equals("188")){
			returnString="中国移动";   //中国移动
		}
		if(preMobile.equals("130") ||  preMobile.equals("131") ||
				preMobile.equals("132") || preMobile.equals("145")  || preMobile.equals("155")
				|| preMobile.equals("156") || preMobile.equals("185")  || preMobile.equals("186")){
			returnString="中国联通";   //中国联通
		}
		if(preMobile.equals("133") ||  preMobile.equals("153") ||
				preMobile.equals("180") || preMobile.equals("181") || preMobile.equals("189")){
			returnString="中国电信";   //中国电信
		}
		return returnString;
	}
}
