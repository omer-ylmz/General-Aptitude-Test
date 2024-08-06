package com.gyt.examservice;

import com.gyt.corepackage.annotations.EnableSecurity;
import com.gyt.corepackage.utils.constants.Paths;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {Paths.ConfigurationBasePackage,Paths.Exam.ServiceBasePackage})
@EnableJpaAuditing
@EnableSecurity
@EnableFeignClients
@EnableScheduling
public class ExamserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamserviceApplication.class, args);
	}

}
