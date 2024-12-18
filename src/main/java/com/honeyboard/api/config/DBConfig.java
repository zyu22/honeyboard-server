package com.honeyboard.api.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages={"com.honeyboard.api.*.model.mapper"})
public class DBConfig {

}
