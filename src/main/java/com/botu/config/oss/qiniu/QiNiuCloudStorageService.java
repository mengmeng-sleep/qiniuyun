package com.botu.config.oss.qiniu;

import com.botu.config.oss.CloudStorageService;
import com.botu.config.oss.qiniu.handler.QiNiuAsyncUpCompletionHandler;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 七牛文件上传服务
 * @ClassName QiNiuCloudStorageService
 * @Author ZX
 * @Date 2020/4/2 10:47
 * @Version 1.0.0
 **/
@Slf4j
public class QiNiuCloudStorageService extends CloudStorageService {

    private QiNiuICloudStorageConfig config;

    private Configuration cfg = new Configuration(Region.autoRegion());

    private UploadManager uploadManager;

    private Auth auth;

    private String token;

    public QiNiuCloudStorageService(QiNiuICloudStorageConfig cloudStorageConfig){
        this.config = cloudStorageConfig;
        uploadManager = new UploadManager(cfg);
        auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        token = getFileUploadToken();
    }

    @Override
    public String getFileUploadToken() {
        return auth.uploadToken(config.getBucketName());
    }

    @Override
    public String getFileUploadToken(StringMap params) {
        //Token有效时长
        long expireSeconds = 180;
        return auth.uploadToken(config.getBucketName(), null, expireSeconds, params);
    }

    @Override
    public String upload(byte[] data, String path) throws QiniuException {
        //可选直接、字节、流、及断点上传
        return byteUpload(data, path);
    }

    @Override
    public String asyncUpload(byte[] data, String suffix, String mimeType, StringMap params) throws IOException {
        String path = getPath(config.getPrefix(), suffix);
        uploadManager.asyncPut(data, path, token, params, mimeType, false, new QiNiuAsyncUpCompletionHandler());
        return config.getDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) throws QiniuException {
        return upload(data, getPath(config.getPrefix(), suffix));
    }

    @Override
    public String upload(InputStream inputStream, String path) throws IOException {
        byte[] data = IOUtils.toByteArray(inputStream);
        return this.upload(data, path);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) throws IOException {
        return upload(inputStream, getPath(config.getPrefix(), suffix));
    }

    @Override
    public String getAccessUrl(String fileUrl) {
        //设置连接访问时长 单位秒。默认3600s
        return auth.privateDownloadUrl(fileUrl, 3600);
    }

    /**
     * 直接上传
     * @param file
     * @param fileName
     * @return
     * @throws QiniuException
     */
    private String directUpload(File file, String fileName) throws QiniuException {
        Response response = uploadManager.put(file, fileName, token);
        return getUploadFileAccessPath(response);
    }

    /**
     * 字节上传
     * @param data
     * @param fileName
     * @return
     * @throws QiniuException
     */
    private String byteUpload(byte[] data, String fileName) throws QiniuException {
        Response response = uploadManager.put(data, fileName, token);
        data = null;
        return getUploadFileAccessPath(response);
    }

    /**
     * 流式上传
     * @param stream 文件流
     * @param key    上传文件保存的文件名
     * @param params 自定义参数，如 params.put("x:foo", "foo")
     * @param mime   指定文件mimetype
     */
    private String inputStreamUpload(InputStream stream, String key, StringMap params, String mime) throws QiniuException {
        Response response = uploadManager.put(stream, key, token, params, mime);
        return getUploadFileAccessPath(response);
    }

    /**
     * 流上传
     * @param inputStream
     * @param fileName
     * @return
     * @throws QiniuException
     */
    private String inputStream(InputStream inputStream, String fileName) throws QiniuException {
        Response response = uploadManager.put(inputStream, fileName, token, null, null);
        return getUploadFileAccessPath(response);
    }

    /**
     * 断点续传
     * @return
     */
    private String resumeUpload(File file, String fileName) throws IOException {
        Configuration cfg = new Configuration(Region.autoRegion());
        String localTempDir = Paths.get(System.getenv("java.io.tmpdir"), config.getBucketName()).toString();
        //设置断点续传文件进度保存目录
        FileRecorder fileRecorder = new FileRecorder(localTempDir);
        UploadManager uploadManager = new UploadManager(cfg, fileRecorder);

        //这里可以使用直接、字节、文件流上传方式（这里选择直接上传）
        Response response = uploadManager.put(file, fileName, token);

        return getUploadFileAccessPath(response);
    }

    @Override
    public void clearBucket() throws QiniuException {
       /* List fileNameList = findFileList();
        String[] fileNameArray = (String[]) fileNameList.toArray(new String[]{});
        batchDeleteFile(fileNameArray);*/
    }

    private List findFileList(){
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(config.getBucketName(), config.getPrefix(), limit, delimiter);
        List<String> fileNameList = new ArrayList<>();
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                fileNameList.add(item.key);
            }
        }
        return fileNameList;
    }

/*    private void batchDeleteFile(String[] keyList) throws QiniuException {
        if(ArrayUtils.isEmpty(keyList)){
            return;
        }
        BucketManager bucketManager = new BucketManager(auth, cfg);
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        batchOperations.addDeleteOp(config.getBucketName(), keyList);
        Response response = bucketManager.batch(batchOperations);
        if(!response.isOK()){
            throw new RuntimeException("批量删除文件失败");
        }
    }*/

    /**
     * 解析七牛云上传文件结果
     * @param response
     * @return
     * @throws QiniuException
     */
    private String getUploadFileAccessPath(Response response) throws QiniuException {
        if (!response.isOK()) {
            throw new RuntimeException("上传七牛出错：" + response.toString());
        }

        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return config.getDomain() + "/" + putRet.key;
    }

}
