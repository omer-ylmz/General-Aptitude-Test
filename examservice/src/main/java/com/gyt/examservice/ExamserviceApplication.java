package com.gyt.examservice;

import com.gyt.corepackage.annotations.EnableSecurity;
import com.gyt.corepackage.utils.constants.Paths;
import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import rpccommon.GrpcConfig;

@SpringBootApplication(scanBasePackages = {Paths.ConfigurationBasePackage,Paths.Exam.ServiceBasePackage})
@EnableJpaAuditing
@EnableSecurity
@EnableFeignClients
@EnableScheduling
@Import(GrpcConfig.class)
public class ExamserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamserviceApplication.class, args);
	}

	@Bean
	public GrpcAuthenticationReader grpcAuthenticationReader(){
		return new BasicGrpcAuthenticationReader();
	}

}
