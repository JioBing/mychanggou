package com.changgou.util;

import com.changgou.file.FastDFSFile;
import jdk.nashorn.internal.ir.LiteralNode;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @ClassName FastDFSClientUtil
 * @Description 实现文件上传的工具类
 * @Author huangpengbing
 * @Date 2024/9/3 20:50
 * @Version 1.0
 **/


@Component
public class FastDFSClientUtil {


    /*初始化tracker，负责负载均衡调度*/
    static {
        try {
            String path = new ClassPathResource("fdfs_client.conf").getPath();
            ClientGlobal.init(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MyException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取StorageClient
     *
     * @return
     */
    public static StorageClient getStorageClient() {
        // 首先创建Tracker调度的客户端对象
        TrackerClient trackerClient = new TrackerClient();
        try {
            // 接着尝试和Tracker建立连接
            TrackerServer trackerServer = trackerClient.getConnection();
            // 用trackerServer对象创建storage客户端对象
            StorageClient storageClient = new StorageClient(trackerServer, null);
            return storageClient;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 文件上传方法
     *
     * @param file
     * @return
     */
    public static String[] upload(FastDFSFile file) {

        // 文件的作者信息
        NameValuePair[] nameValuePairs = new NameValuePair[1];
        NameValuePair nameValuePair = new NameValuePair("huangpengbing");
        nameValuePairs[0] = nameValuePair;

        // 创建TrackerClient客户端对象
        TrackerClient trackerClient = new TrackerClient();
        try {
            // 通过TrackerClient对象获取TrackerServer信息
            TrackerServer trackerServer = trackerClient.getConnection();
            // 创建StorageClient对象，通过trackerServer与其建立连接
            StorageClient storageClient = new StorageClient(trackerServer, null);

            // 上传文件到storage
            /**
             * upload file to storage server (by file buff)
             * Params:
             *      file_buff – file content/buff
             *      file_ext_name – file ext name, do not include dot(.)
             *      meta_list – meta info array
             * Returns:
             * 2 elements string array if success:
             *      results[0]: the group name to store the file
             *      results[1]: the new created filename
             *      return null if fail
             */
            String[] strings = storageClient.upload_file(file.getContent(), file.getExt(), nameValuePairs);

            return strings;
        } catch (IOException | MyException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 通过字节数组下载文件
     *
     * @param fileName 你希望下载下来的文件名
     * @param fileData 字节数组
     * @param response
     * @throws IOException
     */
    public static void downloadByteArray(String fileName, byte[] fileData, HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8").replace("+", "%20") + "\"");

        try (OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            int chunkSize = 1024; // 每块的大小
            int offset = 0;
            while (offset < fileData.length) {
                int length = Math.min(chunkSize, fileData.length - offset);
                toClient.write(fileData, offset, length);
                offset += length;
            }
            toClient.flush();
        }
    }


    /**
     * 检测 HTTP 请求是否来自 Microsoft 浏览器（例如 Internet Explorer、Edge）
     *
     * @param request
     * @return
     */
    private static boolean isMSBrowser(HttpServletRequest request) {
        // 检查 userAgent 是否为 null
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return false;
        }
        String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};
        // 将 userAgent 转换为小写以进行大小写不敏感的比较
        userAgent = userAgent.toLowerCase();
        for (String signal : IEBrowserSignals) {
            if (userAgent.contains(signal.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
