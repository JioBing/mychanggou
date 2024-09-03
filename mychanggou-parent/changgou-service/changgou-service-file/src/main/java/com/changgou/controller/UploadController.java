package com.changgou.controller;

import com.changgou.service.UploadService;
import entity.Result;
import entity.StatusCode;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetSocketAddress;

/**
 * @ClassName UploadController
 * @Description 文件上传
 * @Author huangpengbing
 * @Date 2024/9/3 21:31
 * @Version 1.0
 **/


@RestController
@RequestMapping(path = "")
@CrossOrigin
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

    @PostMapping(path = "/getFileInfo")
    public Result getFileInfo(@RequestParam(value = "groupName") String groupName, @RequestParam(value = "remoteFileName") String remoteFileName) {

        FileInfo fileInfo = uploadService.getFileInfo(groupName, remoteFileName);
        return new Result(true, StatusCode.OK, "获取文件信息成功", fileInfo);
    }

    @GetMapping(path = "/downloadFile")
    public Result downloadFile(@RequestParam(value = "groupName") String groupName, @RequestParam(value = "remoteFileName") String remoteFileName) {

        uploadService.downLoadFile(groupName, remoteFileName);
        return new Result();
    }

    @DeleteMapping(path = "/deleteFile")
    public Result deleteFile(@RequestParam(value = "groupName") String groupName, @RequestParam(value = "remoteFileName") String remoteFileName) {

        int i = uploadService.deleteFile(groupName, remoteFileName);
        return new Result(true, StatusCode.OK, "删除成功", i);
    }

    @GetMapping(path = "/getStorages/{groupName}")
    public Result getStorages(@PathVariable(value = "groupName") String groupName) {

        InetSocketAddress inetSocketAddress = uploadService.getStorages(groupName);
        return new Result(true, StatusCode.OK, "查到StorageServer了", inetSocketAddress);
    }

    @PostMapping(path = "/getServerInfo")
    public Result getServerInfo(@RequestParam(value = "groupName") String groupName, @RequestParam(value = "remoteFileName") String remoteFileName) {

        ServerInfo[] serverInfo = uploadService.getServerInfo(groupName, remoteFileName);
        return new Result(true, StatusCode.OK, "获取storages组信息成功", serverInfo);
    }


    @GetMapping(path = "/getTrackerUrl")
    public Result getTrackerUrl() {

        String trackerUrl = uploadService.getTrackerUrl();
        return new Result(true, StatusCode.OK, "获取tracker服务地址成功", trackerUrl);
    }

}
