package com.gyt.managementservice;

import com.gyt.corepackage.utils.constants.Paths;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {Paths.ConfigurationBasePackage,Paths.Management.ServiceBasePackage})
public class ManagementserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementserviceApplication.class, args);
	}

}
