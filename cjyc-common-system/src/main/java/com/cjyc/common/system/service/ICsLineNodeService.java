package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.LineNode;

import java.util.List;
import java.util.Set;

public interface ICsLineNodeService {


    List<LineNode> getGuideLine(Set<String> citySet, String city);
}
