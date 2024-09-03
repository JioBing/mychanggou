package com.changgou;

import org.apache.http.util.Args;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName FileApplication
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/9/3 19:20
 * @Version 1.0
 **/


@SpringBootApplication
@EnableEurekaClient
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }
}
