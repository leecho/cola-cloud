package com.honvay.cola.cloud.framework.util;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class FileUtils extends org.apache.commons.io.FileUtils {

    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * NIO way
     */
    public static byte[] toByteArray(String filename) {

        File f = new File(filename);
        if (!f.exists()) {
            log.error("文件未找到！" + filename);
            throw new RuntimeException("文件未找到");
        }
        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            throw new RuntimeException("文件读写错误");
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                throw new RuntimeException("文件读写错误");
            }
            try {
                fs.close();
            } catch (IOException e) {
                throw new RuntimeException("文件读写错误");
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath 原文件路径
     * @param newPath 复制后路径
     */
    public static boolean copyFile(String oldPath, String newPath) {
        boolean result = false;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            String _newPath = newPath.substring(0, newPath.lastIndexOf("\\"));
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                File newFile = new File(_newPath);
                if (!newFile.exists()) {
                    newFile.mkdirs();
                    System.out.println("create");
                }
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            if (new File(newPath).exists()) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 复制文件夹
     *
     * @param oldPath 原文件路径
     * @param newPath 复制后路径
     */
    public static boolean copyFolder(String oldPath, String newPath) {
        boolean result = false;
        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
            if (new File(newPath).exists()) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 根据指定路径获取File对象
     *
     * @param path:指定路径
     * @return 该文件File对象
     */
    public static File getFile(String path) {
        return new File(path);
    }


    /**
     * 从ClassPath路径获取文件
     *
     * @param path 指定路径
     * @return 该文件File对象
     */
    public static File getFileFromClassPath(String path) {
        return getFile(ClasseUtils.getClassPath() + path);
    }


    /**
     * 根据charset编码读取文件
     *
     * @param file    指定File对象
     * @param charset 指定编码
     * @return 指定file对象内容
     */
    public static String read(File file, Charset charset) {
        String result = null;
        try {
            result = org.apache.commons.io.FileUtils.readFileToString(file, charset.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 从classpath中读取文件
     *
     * @param path:文件路径
     * @return 文件内容
     */
    public static String readFromClassPath(String path) {
        return read(getFileFromClassPath(path), Charsets.UTF_8);
    }

    /**
     * 输出文件到指定地址
     *
     * @param target  输出地址
     * @param content 要输出内容
     * @throws IOException
     */
    public static void write(String target, String content) throws IOException {
        File file = new File(target);
        File directory = file.getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(content);
        writer.flush();
        writer.close();
        fw.close();
    }

    public static String readByResourceLoader(String path) {
        String content = null;
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(path);
            for (Resource resource : resources) {
                InputStream stream = resource.getInputStream();// 获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
                if (log.isInfoEnabled()) {
                    log.info("读取的文件流  [" + stream + "]");
                }
                content = IOUtils.toString(stream, "UTF-8");
            }
        } catch (Exception e) {
            log.error("获取模板错误", e);
        }
        return content;
    }

}