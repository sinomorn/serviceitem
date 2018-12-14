package com.eports.serviceitem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.eports.serviceitem")
public class ServiceitemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceitemApplication.class, args);
    }
}
