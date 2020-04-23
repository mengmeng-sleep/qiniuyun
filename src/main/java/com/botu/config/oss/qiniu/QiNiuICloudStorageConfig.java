package com.botu.config.oss.qiniu;

import com.botu.config.oss.ICloudStorageConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 七牛云存储服务配置
 * @ClassName QiNiuCloudStorageConfig
 * @Author ZX
 * @Date 2020/4/2 9:36
 * @Version 1.0.0
 **/
@Component
public class QiNiuICloudStorageConfig implements ICloudStorageConfig {

    /**
     * 七牛绑定的域名
     */
    @Value("${oss.qiniu.domain}")
    private String domain;

    /**
     * 七牛路径前缀
     */
    @Value("${oss.qiniu.prefix}")
    private String prefix;

    /**
     * 七牛ACCESS_KEY
     */
    @Value("${oss.qiniu.accessKey}")
    private String accessKey;

    /**
     * 七牛SECRET_KEY
     */
    @Value("${oss.qiniu.secretKey}")
    private String secretKey;

    /**
     * 七牛存储空间名
     */
    @Value("${oss.qiniu.bucketName}")
    private String bucketName;

    public String getDomain() {
        return this.domain;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getBucketName() {
        return this.bucketName;
    }
}
