package com.changgou.service.impl;

import com.changgou.file.FastDFSFile;
import com.changgou.service.UploadService;
import com.changgou.util.FastDFSClientUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName UploadService
 * @Description 文件上传业务
 * @Author huangpengbing
 * @Date 2024/9/3 21:55
 * @Version 1.0
 **/

@Service
public class UploadServiceImpl implements UploadService {

    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     */
    @Override
    public Result uploadFile(MultipartFile multipartFile) {
        String[] uploads;
        try {
            if (multipartFile.isEmpty()) {
                return new Result(false, StatusCode.ERROR, "请选择文件！");
            }
            byte[] bytes = multipartFile.getBytes();  // 文件内容的字节数组
            String name = multipartFile.getName();   // 是从 Apache Commons FileUpload 库中的 FileItem 类派生的，返回表单字段的名称file（而不是文件名）。
            String contentType = multipartFile.getContentType();  // image/jpeg
            String originalFilename = multipartFile.getOriginalFilename();  // 文件名称 pexels-bertellifotografia-573299.jpg
            Resource resource = multipartFile.getResource(); // 显示multipartFile对象及其里面包含的内容
            long size = multipartFile.getSize();  // 文件大小，显示单位为字节，2.06 MB (2,163,508 字节)
            InputStream inputStream = multipartFile.getInputStream();   // 文件输入流
            System.out.println("获取multipartFile携带参数信息：successfully!");

            String[] split = originalFilename.split("\\.");
            String extension = split[1];  // 获取文件扩展名

            FastDFSFile file = new FastDFSFile(originalFilename, bytes, extension);
            uploads = FastDFSClientUtil.upload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Result result = new Result(true, StatusCode.OK, "上传成功", uploads);
        return result;
    }
}
