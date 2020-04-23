package com.botu.api;

import com.botu.service.IFileService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传测试类
 * @ClassName FileApi
 * @Author ZX
 * @Date 2020/4/2 11:27
 * @Version 1.0.0
 **/
@Slf4j
@RestController
@RequestMapping("/V1.0.0/file/operate")
public class FileApi  {

    @Value("${oss.file.image-type-format}")
    private String imageTypeFormat;

    @Autowired
    private IFileService fileService;

    /**
     * 文件图片上传接口
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(MultipartFile file) throws IOException {
        if(file == null){
            return "";
        }
        if(StringUtils.isNotBlank(imageTypeFormat)){
            log.info("开启了图片格式校验");
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            if(imageTypeFormat.toLowerCase().indexOf(suffix.toLowerCase()) < 0){
                return "";
            }
        }
        return fileService.upload(file);
    }

    /**
     * 图片批量上传
     * @param files
     * @return
     */
    @PostMapping("/batchUpload")
    public List batchUpload(List<MultipartFile> files) throws IOException {
        List list=new ArrayList();
        if(files == null || files.size() == 0){
            return list;
        }
        if(StringUtils.isNotBlank(imageTypeFormat)){
            log.info("开启了图片格式校验");
            for(MultipartFile file : files){
                String originalFilename = file.getOriginalFilename();
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                if(imageTypeFormat.toLowerCase().indexOf(suffix.toLowerCase()) < 0){
                    return list;
                }
            }
        }
        return fileService.batchUpload(files);
    }

    /**
     * 文件上传批量接口
     * @param files
     * @return
     */
    @PostMapping("/fileBatchUpload")
    public List fileBatchUpload(List<MultipartFile> files) throws IOException {
        List list=new ArrayList();
        if(files == null || files.size() == 0){
            return list;
        }
        return fileService.fileBatchUpload(files);
    }





}
