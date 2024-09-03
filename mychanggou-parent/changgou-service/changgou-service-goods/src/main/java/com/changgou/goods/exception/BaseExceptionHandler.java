package com.changgou.goods.exception;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName BaseExceptionHandler
 * @Description TODO
 * @Author huangpengbing
 * @Date 2024/8/23 17:05
 * @Version 1.0
 **/


@ControllerAdvice  // 可以对Controller中使用到@RequestMapping注解的方法做逻辑处理。
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class) // 明捕获哪些异常，对哪些异常进行处理。
    @ResponseBody // 异常处理后的结果以json格式返回给浏览器
    public Result error(Exception e) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }

}
