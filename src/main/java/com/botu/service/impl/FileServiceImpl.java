package com.botu.service.impl;

import com.botu.config.oss.OSSFactory;
import com.botu.config.oss.qiniu.QiNiuICloudStorageConfig;
import com.botu.service.IFileService;
import com.qiniu.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 七牛文件操作服务
 * @Author ZX
 * @Date 2020/4/7 9:46
 * @Version 1.0.0
 **/
@Slf4j
@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private OSSFactory ossFactory;

    @Autowired
    private QiNiuICloudStorageConfig qiNiuICloudStorageConfig;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        return qiniuUpload(originalFilename, file);
    }

    @Override
    public List batchUpload(List<MultipartFile> files) throws IOException {
        List list=new ArrayList<MultipartFile>();
        for(MultipartFile file : files){

            String originalFilename = file.getOriginalFilename();
            String path = qiniuUpload(originalFilename, file);
            list.add(path);
        }
        return list;
    }

    @Override
    public List fileBatchUpload(List<MultipartFile> files) throws IOException {
        List list=new ArrayList<MultipartFile>();
        for(MultipartFile file : files){

            String originalFilename = file.getOriginalFilename();
            String path = qiniuUpload(originalFilename, file);
            System.gc();
            list.add(path);

        }
        return list;
    }



    private String qiniuUpload(String originalFilename, MultipartFile file) throws IOException {
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String path = ossFactory.build(qiNiuICloudStorageConfig).uploadSuffix(IOUtils.toByteArray(file.getInputStream()), suffix);
        file = null;
        System.gc();
        return path;
    }

}
