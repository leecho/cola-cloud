package com.honvay.cola.framework.storage.impl;

import com.honvay.cola.framework.storage.FileStorage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;

/**
 * 本地文件上传
 *
 * @author LIQIU
 * @date 2017-12-14-上午10:52
 */
public class LocalFileStorage implements FileStorage {

    private String path;

    private Boolean haveCreatePath = false;

    public LocalFileStorage(String path) {
        this.path = path;
    }

    @Override
    public void store(byte[] fileBytes, String key) {
        path = getFileUploadPath();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(path + key));
            fileOutputStream.write(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException("Write file error!");
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("Write file error!");
                }
            }
        }
    }

    @Override
    public void store(InputStream input, String key) {
        File file = new File(this.getFileUploadPath(),key);
        if(file.exists()){
            throw new IllegalArgumentException("相同KEY的文件已存在");
        }
        FileOutputStream output = null;
        try {
            file.createNewFile();
            output =  new FileOutputStream(file);
            IOUtils.copy(input,output);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("存储文件失败：" + e);
        }finally {
            if(output != null){
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public byte[] getBytes(String key) {
        String filePath = getFileUploadPath() + key;
        File file = new File(filePath);
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException("Read file error!");
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Read file error!");
            }
        }
    }

    @Override
    public void remove(String key) {
        new File(this.getFileUploadPath(),key).delete();
    }

    @Override
    public InputStream getInputStream(String key) {
        try {
            return new FileInputStream(new File(this.getFileUploadPath(),key));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getDownloadUrl(String key) {
        throw new UnsupportedOperationException("本地存储不支持该操作");
    }

    private String getFileUploadPath() {
        //如果没有写文件上传路径,保存到临时目录
        if (StringUtils.isEmpty(path)) {
            return System.getProperty("java.io.tmpdir");
        } else {
            //判断有没有结尾符,没有得加上
            if (!path.endsWith(File.separator)) {
                path = path + File.separator;
            }
            //判断目录存不存在
            if (!haveCreatePath) {
                File file = new File(path);
                file.mkdirs();
                haveCreatePath = true;
            }
            return path;
        }
    }

}
