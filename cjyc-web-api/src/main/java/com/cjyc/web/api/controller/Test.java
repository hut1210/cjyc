package com.cjyc.web.api.controller;

import com.google.common.collect.Lists;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args){
        //库里查询出来
        ArrayList<Long> list1 = Lists.newArrayList();
        /*list1.add(1L);
        list1.add(2L);
        list1.add(3L);*/
        //传过来
        ArrayList<Long> list2 = Lists.newArrayList();
        list2.add(1L);
        list1.removeAll(list2);
        System.out.println(list1);
    }
}