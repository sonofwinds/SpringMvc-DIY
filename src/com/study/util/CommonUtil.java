package com.study.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
	 /**
     * 扫描包下的所有文件
     *
     * @param Package
     */
    public static List<String> scanPackage(List<String> packages , Class cla , String Package) {
    	if(packages == null) {
    		packages = new ArrayList<String>();
    	}
        URL url = cla.getClassLoader().getResource("/" + replaceTo(Package));// 将所有的.转义获取对应的路径
        String pathFile = url.getFile();
        File file = new File(pathFile);
        String fileList[] = file.list();
        for (String path : fileList) {
            File eachFile = new File(pathFile + path);
            if (eachFile.isDirectory()) {
            	scanPackage(packages,cla,Package + "." + eachFile.getName());
            } else {
            	packages.add(Package + "." + eachFile.getName());
            }
        }
		return packages;
    }
    private static String replaceTo(String path) {
        return path.replaceAll("\\.", "/");
    }
}
