package com.botu.config.oss;

import com.qiniu.common.QiniuException;
import com.qiniu.util.StringMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @ClassName CloudStorageService
 * @Author ZX
 * @Date 2020/4/2 10:40
 * @Version 1.0.0
 **/
public abstract class CloudStorageService {

    /**
     * 文件路径
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
    public String getPath(String prefix, String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //文件路径
        String path = String.format(System.currentTimeMillis() + "").trim() + uuid;

        if (prefix != "" && prefix != null) {
            path = prefix + "/" + path;
        }
        return path + suffix;
    }

    /**
     * 获获取文件上传文件 Token 方法
     * @return
     */
    public abstract String getFileUploadToken();

    /**
     * 可添加自定义参数并存在时效的获取文件上传文件 Token 方法
     * @param params 自定参数
     * @return
     */
    public abstract String getFileUploadToken(StringMap params);

    /**
     * 文件上传
     * @param data    文件字节数组
     * @param path    文件路径，包含文件名
     * @return        返回http地址
     */
    public abstract String upload(byte[] data, String path) throws QiniuException;

    /**
     * 文件上传
     * @param data     文件字节数组
     * @param suffix   文件后缀
     * @param mimeType 文件mimetype
     * @param params   自定义参数
     * @return         返回http地址
     */
    public abstract String asyncUpload(byte[] data, String suffix, String mimeType, StringMap params) throws IOException;

    /**
     * 文件上传
     * @param data     文件字节数组
     * @param suffix   后缀
     * @return         返回http地址
     */
    public abstract String uploadSuffix(byte[] data, String suffix) throws QiniuException;

    /**
     * 文件上传
     * @param inputStream   字节流
     * @param path          文件路径，包含文件名
     * @return              返回http地址
     */
    public abstract String upload(InputStream inputStream, String path) throws IOException;

    /**
     * 文件上传
     * @param inputStream  字节流
     * @param suffix       后缀
     * @return             返回http地址
     */
    public abstract String uploadSuffix(InputStream inputStream, String suffix) throws IOException;

    /**
     * 私密空间文件访问
     * @param fileUrl 文件路径
     * @return
     */
    public abstract String getAccessUrl(String fileUrl);

    /**
     * 清空空间（慎用）
     */
    public abstract void clearBucket() throws QiniuException;

}
