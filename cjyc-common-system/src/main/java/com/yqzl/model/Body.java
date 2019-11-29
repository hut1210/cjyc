package com.yqzl.model;

/**
 * @Author:Hut
 * @Date:2019/11/29 15:17
 */
public class Body {

    public static String getOutTransferBody(OutTransfer outTransfer){

        return "<body><pay_acno>"+outTransfer.getPay_acno()+"</pay_acno>" +
                "<pay_acname>"+outTransfer.getPay_acname()+"</pay_acname>"+
                "<rcv_bank_name>"+outTransfer.getRcv_bank_name()+"</rcv_bank_name>" +
                "<rcv_acno>"+outTransfer.getRcv_acno()+"</rcv_acno>" +
                "<rcv_acname>"+outTransfer.getRcv_acname()+"</rcv_acname>"+
                "<cur_code>CNY</cur_code>" +
                "<amt>"+outTransfer.getAmt()+"</amt>" +
                "<cert_no>"+outTransfer.getCert_no()+"</cert_no>" +
                "<bank_flag>"+outTransfer.getBank_flag()+"</bank_flag></body>";
    }
}
