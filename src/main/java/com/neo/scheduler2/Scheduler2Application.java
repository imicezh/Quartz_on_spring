package com.neo.scheduler2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * there is no respective superior and inferior container in springboot
 */

@EnableTransactionManagement
@MapperScan(basePackages={"com.neo.scheduler2.dao","com.neo.scheduler2.backend.dao"})
@SpringBootApplication
public class Scheduler2Application {

	public static void main(String[] args) {
		SpringApplication.run(Scheduler2Application.class, args);
	}

}
