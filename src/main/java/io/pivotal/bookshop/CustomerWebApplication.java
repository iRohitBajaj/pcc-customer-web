package io.pivotal.bookshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CustomerWebApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CustomerWebApplication.class);
	}

	public static void main(String[] args) {
		/*org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();
		logger.setLevel(org.apache.log4j.Level.toLevel("TRACE"));*/
		SpringApplication.run(CustomerWebApplication.class, args);
	}


}
