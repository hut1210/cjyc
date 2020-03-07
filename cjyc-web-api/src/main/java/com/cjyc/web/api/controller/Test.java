package com.cjyc.web.api.controller;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args){
        List<String> list1 = new ArrayList<String>();
        list1.add("A");
        list1.add("B");
        list1.add("C");

        List<String> list2 = new ArrayList<String>();
        list2.add("C");
        list2.add("B");
        list2.add("D");
        List<String> list3 = null;
                // 差集
        list1.removeAll(list2);
        System.out.println(CollectionUtils.isEmpty(list3));

    }
}