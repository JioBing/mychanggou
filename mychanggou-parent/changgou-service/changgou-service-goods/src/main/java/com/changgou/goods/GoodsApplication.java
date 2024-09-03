package com.changgou.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName GoodsApplication
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/8/20 4:34
 * @Version 1.0
 **/


@SpringBootApplication
@EnableEurekaClient  // 开启Eureka客户端
@MapperScan(basePackages = {"com.changgou.goods.dao"})
public class GoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class);
    }
}
