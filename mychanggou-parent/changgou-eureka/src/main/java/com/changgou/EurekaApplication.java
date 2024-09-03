package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName EurekaApplication
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/8/20 2:27
 * @Version 1.0
 **/


@SpringBootApplication
@EnableEurekaServer  /*启用 Eureka 服务器功能，使得应用变成一个 Eureka 服务注册中心。通过这个注解，Spring Boot 应用将成为一个 Eureka 服务器实例，允许其他微服务注册到它上面。*/
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class);
    }
}
