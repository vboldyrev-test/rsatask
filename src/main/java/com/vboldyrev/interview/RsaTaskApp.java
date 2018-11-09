package com.vboldyrev.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RsaTaskApp {
    public static void main(String[] args) {
        SpringApplication.run(RsaTaskApp.class, args);
    }
}
