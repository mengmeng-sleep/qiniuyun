package com.botu.service;

import com.qiniu.util.StringMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 七牛文件操作服务
 * @Author ZX
 * @Date 2020/4/7 9:46
 * @Version 1.0.0
 **/
public interface IFileService {

    /**
     * 文件图片上传接口
     * @param file
     * @return
     */
    String upload(MultipartFile file) throws IOException;

    /**
     * 图片批量上传
     * @param files
     * @return
     */
   List batchUpload(List<MultipartFile> files) throws IOException;

    /**
     * 文件上传批量接口
     * @param files
     * @return
     */
    List fileBatchUpload(List<MultipartFile> files) throws IOException;


}
