package com.schoolmap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.schoolmap.mapper")
@SpringBootApplication
public class SchoolmapApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolmapApplication.class, args);
        System.out.println("校园导航启动成功");
    }

}
