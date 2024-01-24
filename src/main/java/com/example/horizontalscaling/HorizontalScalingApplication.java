package com.example.horizontalscaling;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRabbit
@EnableScheduling
@SpringBootApplication
public class HorizontalScalingApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(HorizontalScalingApplication.class)
                .run(args);
    }

}
