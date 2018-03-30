package com.honvay.cola.cloud.framework.util;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZipUitls 工具类
 *
 * @author YRain
 * @createdate 2017-12-15
 */
public class ZipUtils {

    static final int BUFFER = 8192;

    /**
     * 压缩
     *
     * @param srcPath 待压缩路径
     * @param dstPath 压缩输出路径
     * @throws IOException
     */
    public static void compress(String srcPath, String dstPath) throws IOException {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(srcPath + "不存在！");
        }
        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        try {
            out = new FileOutputStream(dstFile);
            CheckedOutputStream cos = new CheckedOutputStream(out, new CRC32());
            zipOut = new ZipOutputStream(cos);
            String baseDir = "";
            compress(srcFile, zipOut, baseDir);
        } finally {
            if (null != zipOut) {
                zipOut.close();
                out = null;
            }

            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 压缩
     *
     * @param file    待压缩File对象
     * @param zipOut  ZipOutputStream对象
     * @param baseDir 压缩基础路径
     * @throws IOException
     */
    private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }


    /**
     * 压缩目录
     *
     * @param dir     待压缩目录路径
     * @param zipOut  ZipOutputStream对象
     * @param baseDir 压缩基础路径
     * @throws IOException
     */
    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], zipOut, baseDir + dir.getName() + "/");
        }
    }


    /**
     * 压缩文件
     *
     * @param file    待压缩File对象
     * @param zipOut  ZipOutputStream对象
     * @param baseDir 压缩基础路径
     * @throws IOException
     */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (!file.exists()) {
            return;
        }
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }

        } finally {
            if (null != bis) {
                bis.close();
            }
        }
    }

}
