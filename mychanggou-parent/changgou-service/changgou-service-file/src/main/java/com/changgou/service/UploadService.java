package com.changgou.service;


import com.changgou.util.FastDFSClientUtil;
import entity.Result;
import org.springframework.stereotype.Repository;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface UploadService {

    Result uploadFile(MultipartFile multipartFile);


}
