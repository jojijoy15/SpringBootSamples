package com.learning.spring.company.configuration.datasource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.ManyToMany;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Map;
@Transactional
@Configuration
@EnableJpaRepositories(
        basePackages = "com.learning.spring.company",
        entityManagerFactoryRef = "mySqlDbEntityManager",
        transactionManagerRef = "mySqlDbTransactionManager"
)
public class MySqlConfig {

//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource.mysql")
//    public DataSource mySqlDbDataSource() {
//        return DataSourceBuilder.create().build();
//    }

    @ManyToMany
    @Bean(name = "msql-config")
    @ConfigurationProperties("spring.datasource.mysql")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource mySqlDbDataSource() {
        return dataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mySqlDbEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("mySqlDbDataSource") DataSource datasource) {
        return builder
                .dataSource(datasource)
                .packages("com.learning.spring.company")
                .persistenceUnit("mySqlDB")
                .properties(Map.of("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"))
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager mySqlDbTransactionManager(
            @Qualifier("mySqlDbEntityManager") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}