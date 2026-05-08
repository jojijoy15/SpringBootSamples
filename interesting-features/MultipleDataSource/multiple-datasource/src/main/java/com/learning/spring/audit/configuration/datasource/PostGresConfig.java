package com.learning.spring.audit.configuration.datasource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.learning.spring.audit",
        entityManagerFactoryRef = "postGresDbEntityManager",
        transactionManagerRef = "postGresDbTransactionManager"
)
public class PostGresConfig {

//    @Bean
//    @ConfigurationProperties("spring.datasource.postgresql")
//    public DataSource postGresDbDataSource() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean(name = "postgresql-config")
    @ConfigurationProperties("spring.datasource.postgresql")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource postGresDbDataSource() {
        return dataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean postGresDbEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("postGresDbDataSource") DataSource datasource) {
        return builder
                .dataSource(datasource)
                .packages("com.learning.spring.audit")
                .persistenceUnit("postGresDB")
                .properties(Map.of("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"))
                .build();
    }

    @Bean
    public PlatformTransactionManager postGresDbTransactionManager(
            @Qualifier("postGresDbEntityManager") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}