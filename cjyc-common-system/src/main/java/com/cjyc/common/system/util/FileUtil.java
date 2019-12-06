package com.cjyc.common.system.util;

import java.io.File;
import java.util.ArrayList;

public class FileUtil {

    // path:文件夹存放路径
    // fileNameList:存放获取的文件名
    public void getAllFileName(String path, ArrayList<String> fileNameList) {
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                fileNameList.add(tempList[i].getName());
            }
            if (tempList[i].isDirectory()) {
                getAllFileName(tempList[i].getAbsolutePath(),fileNameList);
            }
        }
        return;
    }
}