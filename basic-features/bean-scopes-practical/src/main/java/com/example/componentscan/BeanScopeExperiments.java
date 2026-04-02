package com.example.componentscan;

import com.example.componentscan.application.ApplicationScoped;
import com.example.componentscan.singleton.SingletonScoped;
import jakarta.servlet.ServletContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class BeanScopeExperiments {


	public static void main(String[] args) {
		// 1) Main app context (normal Spring Boot startup)
		ConfigurableApplicationContext mainContext =
				new SpringApplicationBuilder(BeanScopeExperiments.class)
						.run(args);

		// 2) Secondary context as CHILD of main (inherits parent beans)
		List<String> list = Arrays.stream(args).collect(Collectors.toList());
		list.add("--server.port=8909");
		ConfigurableApplicationContext childContext =
				new SpringApplicationBuilder(ChildConfig.class)
						.run(list.toArray(String[]::new));

		// 3) Close child when main context closes
		mainContext.addApplicationListener(event -> {
			if (event instanceof org.springframework.context.event.ContextClosedEvent) {
				childContext.close();
			}
		});

		System.out.println("Secondary bean = " + childContext.getBean("childBean"));
	}

	@Configuration
	@ComponentScan("com.example.componentscan")
	static class ChildConfig {
		@Bean
		String childBean() {
			return "I am from second context";
		}
	}
}
