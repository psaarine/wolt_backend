package com.woltappsummer.BackendTest;

import com.woltappsummer.BackendTest.DummyData.DummyDataGenerator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


import java.io.File;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class BackendTestApplication {

	static private String filename = "restaurants.json";
	static private String path = System.getProperty("user.dir");
	static private String filePath = path + "/" + filename;

	public static void main(String[] args) {

		try {
			File file = new File(filePath);
			if (file.createNewFile()){
				System.out.println("Didn't find restaurant json file.");
				System.out.println("Generating dummy file");
				DummyDataGenerator generator = new DummyDataGenerator(filePath);
				generator.generateAdvancedDummy();
			} else {
				System.out.println(" File exists.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		SpringApplication.run(BackendTestApplication.class, args);



	}

}
