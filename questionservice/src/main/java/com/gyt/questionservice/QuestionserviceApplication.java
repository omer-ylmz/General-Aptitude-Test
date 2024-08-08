package com.gyt.questionservice;

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
import rpccommon.GrpcConfig;


@SpringBootApplication(scanBasePackages = {Paths.ConfigurationBasePackage,Paths.Question.ServiceBasePackage})
@EnableFeignClients
@EnableJpaAuditing
@EnableSecurity
@Import(GrpcConfig.class)
public class QuestionserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionserviceApplication.class, args);
	}

	@Bean
	public GrpcAuthenticationReader grpcAuthenticationReader(){
		return new BasicGrpcAuthenticationReader();
	}

}
