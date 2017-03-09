package com.centanet.framework.utils;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by vctor2015 on 16/7/15.
 * <p>
 * 描述:文件夹工具
 */
@SuppressWarnings("unused")
public final class FolderUtil {

    private FolderUtil() {
        //Utility Class
    }

    /**
     * 图片缓存大小
     */
    public static String imageCacheSize(File fileDir) {
        return formatSize(getFolderSize(fileDir));
    }

    /**
     * 文件夹缓存大小
     */
    private static long getFolderSize(File fileDir) {
        long size = 0;
        try {
            File[] fileList = fileDir.listFiles();
            for (File file : fileList) {
                if (file.isDirectory()) {
                    size = size + getFolderSize(file);
                } else {
                    size = size + file.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return size;
        }
        return size;
    }

    /**
     * 格式化
     */
    private static String formatSize(long size) {
        double kiloByte = size / 1024;
        if (size == 0) {
            return "0Byte(s)";
        }
        if (kiloByte < 1) {
            return size + "Byte(s)";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        } else if (megaByte > 50) {
            return ">50MB";
        } else {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
    }
}
