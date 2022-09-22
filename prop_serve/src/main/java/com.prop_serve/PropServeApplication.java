package com.prop_serve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.prop_serve.mapper")
//其实保留上面的MapperScan是最省力的，因为如果不在这里写，每个mapper都要自己手动注释@Mapper
//二者都存在的话，就会产生重复注入警告
//@MapperScan("com.baomidou.mybatisplus.samples.quickstart.mapper")

public class PropServeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropServeApplication.class, args);
	}

}
