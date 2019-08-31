package cn.shierblog.spider_zhiping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpiderZhipingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiderZhipingApplication.class, args);
    }

}
