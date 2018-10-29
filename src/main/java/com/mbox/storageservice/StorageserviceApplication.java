package com.mbox.storageservice;

import com.mbox.filter.AuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@ComponentScan("com.mbox.*")
@EntityScan(basePackages = {"com.mbox.*"})
@EnableJpaRepositories(basePackages={"com.mbox.*"})
public class StorageserviceApplication{

	@Bean
	WebMvcConfigurer configurer () {
 		return new WebMvcConfigurer() {
  			@Override
  			public void addResourceHandlers (ResourceHandlerRegistry registry) {
   				registry.addResourceHandler("/ssweb/**").
     			addResourceLocations("classpath:/public/");
  			}
 		};
	}	

	public static void main(String[] args) {
		SpringApplication.run(StorageserviceApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<AuthorizationFilter > filterRegistrationBean() {
		FilterRegistrationBean < AuthorizationFilter > registrationBean = new FilterRegistrationBean();
		AuthorizationFilter authorizationFilter = new AuthorizationFilter();

		registrationBean.setFilter(authorizationFilter);
		registrationBean.addUrlPatterns("/storage/*");
		//registrationBean.setOrder(2); //set precedence
		return registrationBean;
	}
}
