package com.cjyc.common.system.service;

import java.util.List;
import java.util.Set;

public interface ICsLineNodeService {


    List<String> getGuideLine(Set<String> citySet, String city);
}
