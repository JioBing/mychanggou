package com.changgou.service;


import com.changgou.util.FastDFSClientUtil;
import entity.Result;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageServer;
import org.springframework.stereotype.Repository;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetSocketAddress;

@Repository
public interface UploadService {

    Result uploadFile(MultipartFile multipartFile);

    FileInfo getFileInfo(String groupName, String remoteFileName);

    void downLoadFile(String groupName, String remoteFileName);

    int deleteFile(String groupName,String remoteFileName);

    InetSocketAddress getStorages(String groupName);

    ServerInfo[] getServerInfo(String groupName, String remoteFileName);

    String getTrackerUrl();
}
