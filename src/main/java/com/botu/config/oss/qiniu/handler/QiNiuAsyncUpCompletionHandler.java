package com.botu.config.oss.qiniu.handler;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UpCompletionHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * 七牛云异步上传回调
 * @ClassName QiNiuAsyncUpCompletionHandler
 * @Author ZX
 * @Date 2020/4/2 12:51
 * @Version 1.0.0
 **/
@Slf4j
public class QiNiuAsyncUpCompletionHandler implements UpCompletionHandler {

    @Override
    public void complete(String key, Response r) {
        log.info(key);
        try {
            log.info(r.bodyString());
        } catch (QiniuException e) {
            e.printStackTrace();
        }

    }
}
