package com.changgou.util;

import com.changgou.file.FastDFSFile;
import jdk.nashorn.internal.ir.LiteralNode;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
     * 文件上传方法
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

}
