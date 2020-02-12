package com.definesys.dsgc.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ComponentScan(basePackages = {"com.definesys.mpaas","com.definesys.dsgc"})
@SpringBootApplication(exclude = {ValidationAutoConfiguration.class,FreeMarkerAutoConfiguration.class})
@EnableTransactionManagement
public class DsgcappservicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsgcappservicesApplication.class,args);
    }

}
