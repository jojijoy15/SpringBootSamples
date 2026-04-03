package com.example.beanscopes;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BeanScopeExperiments {


	public static void main(String[] args) {
		// 1) Main app context (normal Spring Boot startup)
		ConfigurableApplicationContext mainContext =
				new SpringApplicationBuilder(BeanScopeExperiments.class)
						.run(args);

//		// 2) Secondary context as CHILD of main (inherits parent beans)
//		List<String> list = Arrays.stream(args).collect(Collectors.toList());
//		list.add("--server.port=8909");
//		ConfigurableApplicationContext childContext =
//				new SpringApplicationBuilder(ChildConfig.class)
//						.run(list.toArray(String[]::new));
//
//		// 3) Close child when main context closes
//		mainContext.addApplicationListener(event -> {
//			if (event instanceof org.springframework.context.event.ContextClosedEvent) {
//				childContext.close();
//			}
//		});

//		System.out.println("Secondary bean = " + childContext.getBean("childBean"));
	}

//	@Configuration
//	@ComponentScan("com.example.componentscan")
//	static class ChildConfig {
//		@Bean
//		String childBean() {
//			return "I am from second context";
//		}
//	}
}
