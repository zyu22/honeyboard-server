package com.honeyboard.api.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.honeyboard.api.**.mapper"})
public class DBConfig {

}
