package com.botu.config.oss;

import com.botu.config.oss.qiniu.QiNiuCloudStorageService;
import com.botu.config.oss.qiniu.QiNiuICloudStorageConfig;
import org.springframework.stereotype.Component;

/**
 * 云存储初始化工厂
 * @ClassName OSSFactory
 * @Author ZX
 * @Date 2020/4/2 10:37
 * @Version 1.0.0
 **/
@Component
public class OSSFactory {

    public CloudStorageService build(ICloudStorageConfig cloudStorageConfig){
        //获取云存储配置信息
        if(cloudStorageConfig instanceof QiNiuICloudStorageConfig){
            return new QiNiuCloudStorageService((QiNiuICloudStorageConfig) cloudStorageConfig);
        } else {
            throw new RuntimeException("未知云存储配置");
        }
    }

}
