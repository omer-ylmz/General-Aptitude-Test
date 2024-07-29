package com.gyt.questionservice;

import com.gyt.corepackage.annotations.EnableSecurity;
import com.gyt.corepackage.utils.constants.Paths;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication(scanBasePackages = {Paths.ConfigurationBasePackage,Paths.Question.ServiceBasePackage})
@EnableFeignClients
@EnableJpaAuditing
@EnableSecurity
public class QuestionserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionserviceApplication.class, args);
	}

}
