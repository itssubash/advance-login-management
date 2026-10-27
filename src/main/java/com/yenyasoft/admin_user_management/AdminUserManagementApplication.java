package com.yenyasoft.admin_user_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AdminUserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminUserManagementApplication.class, args);
	}

}
