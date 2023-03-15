package project.bookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import project.bookservice.config.MyBatisConfig;

import java.lang.String;

@SpringBootApplication
@Import(MyBatisConfig.class)
public class BookServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookServiceApplication.class, args);

	}
}
