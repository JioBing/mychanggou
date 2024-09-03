package com.changgou.service.impl;

import com.changgou.file.FastDFSFile;
import com.changgou.service.UploadService;
import com.changgou.util.FastDFSClientUtil;
import entity.Result;
import entity.StatusCode;
import org.apache.http.HttpResponse;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName UploadService
 * @Description 文件上传业务
 * @Author huangpengbing
 * @Date 2024/9/3 21:55
 * @Version 1.0
 **/

@Service
public class UploadServiceImpl implements UploadService {

    private final HttpServletResponse response;

    @Autowired
    public UploadServiceImpl(HttpServletResponse response) {
        this.response = response;
    }

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

    /**
     * 获取文件信息
     *
     * @param groupName      组名 group1
     * @param remoteFileName 远程上完整的文件名，虚拟磁盘路径（逻辑上的）/两级数据目录/文件名 M00/00/00/wKiwgGbXHrmAZ8-FACEDNPA48X4209.jpg
     * @return
     */
    @Override
    public FileInfo getFileInfo(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = FastDFSClientUtil.getStorageClient();
            FileInfo fileInfo = storageClient.get_file_info(groupName, remoteFileName);
            long fileSize = fileInfo.getFileSize(); // 文件大小，单位是字节
            long crc32 = fileInfo.getCrc32(); // 签名
            Date createTimestamp = fileInfo.getCreateTimestamp(); // 文件的创建时间
            String sourceIpAddr = fileInfo.getSourceIpAddr(); // 获取服务器ip地址192.168.176.128
            return fileInfo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MyException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 文件下载
     *
     * @param groupName      组名 group1
     * @param remoteFileName 远程上完整的文件名，虚拟磁盘路径（逻辑上的）/两级数据目录/文件名 M00/00/00/wKiwgGbXHrmAZ8-FACEDNPA48X4209.jpg
     */
    @Override
    public void downLoadFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = FastDFSClientUtil.getStorageClient();
            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
            // 使用 UUID 生成唯一标识符
            String uniqueId = UUID.randomUUID().toString();
            // 获取文件扩展名
            String fileExtension = remoteFileName.substring(remoteFileName.lastIndexOf("."));
            String real = "真实的文件名";
            String fileName = real + "-" + uniqueId + fileExtension;
            FastDFSClientUtil.downloadByteArray(fileName, bytes, response);
            // 得到字节流数组后继续操作
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MyException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 删除文件
     *
     * @param groupName      组名 group1
     * @param remoteFileName 远程上完整的文件名，虚拟磁盘路径（逻辑上的）/两级数据目录/文件名 M00/00/00/wKiwgGbXHrmAZ8-FACEDNPA48X4209.jpg
     */
    @Override
    public int deleteFile(String groupName, String remoteFileName) {
        StorageClient storageClient = FastDFSClientUtil.getStorageClient();
        int i;
        try {
            i = storageClient.delete_file(groupName, remoteFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MyException e) {
            throw new RuntimeException(e);
        }
        return i;
    }


    /**
     * 根据tracker客户端获取组分组信息
     *
     * @param groupName 组名 group1
     * @return
     */
    @Override
    public InetSocketAddress getStorages(String groupName) {
        TrackerClient trackerClient = new TrackerClient();
        try {
            // 创建连接时必要的
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer, groupName);
            int storePathIndex = storeStorage.getStorePathIndex(); // 好像是得到了这组storage的索引下标0，而且是从0开始的
            InetSocketAddress inetSocketAddress = storeStorage.getInetSocketAddress();
            return inetSocketAddress;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据文件组名和文件存储路径获取Storage服务的IP、端口信息
     *
     * @param groupName      组名 group1
     * @param remoteFileName 远程上完整的文件名，虚拟磁盘路径（逻辑上的）/两级数据目录/文件名 M00/00/00/wKiwgGbXHrmAZ8-FACEDNPA48X4209.jpg
     */
    @Override
    public ServerInfo[] getServerInfo(String groupName, String remoteFileName) {
        TrackerClient trackerClient = new TrackerClient();
        try {
            // 创建连接时必要的
            TrackerServer trackerServer = trackerClient.getConnection();
            ServerInfo[] serverInfos = trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
            return serverInfos;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Tracker服务地址
     *
     * @return
     */
    @Override
    public String getTrackerUrl() {

        TrackerClient trackerClient = new TrackerClient();
        try {
            // 创建连接时必要的
            TrackerServer trackerServer = trackerClient.getConnection();
            return "http://" + trackerServer.getInetSocketAddress().getHostString() + ":" + ClientGlobal.getG_tracker_http_port();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
