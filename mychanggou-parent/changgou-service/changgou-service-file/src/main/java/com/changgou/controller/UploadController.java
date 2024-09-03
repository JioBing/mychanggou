package com.changgou.controller;

import com.changgou.service.UploadService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName UploadController
 * @Description 文件上传
 * @Author huangpengbing
 * @Date 2024/9/3 21:31
 * @Version 1.0
 **/


@RestController
@RequestMapping(path = "")
public class UploadController {

    private final UploadService uploadService;
    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(path = "/upload")
    public Result uploadFile(@RequestParam(value = "file") MultipartFile multipartFile) {

        Result result = uploadService.uploadFile(multipartFile);
        return result;
    }

    

}
