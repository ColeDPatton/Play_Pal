package com.example.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.application.file.FileStorageProperties;
/*This is used to run the project.  Do not adjust. 
 * 
 */
@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}

