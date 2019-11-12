package org.csu.mypetstore_springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.csu.mypetstore_springboot.persistence")
public class MypetstoreSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MypetstoreSpringbootApplication.class, args);
    }

}
