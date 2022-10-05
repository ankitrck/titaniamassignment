package com.koshti.titaniam.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(
        basePackages = "com.koshti.titaniam.SecondaryCluster",
        entityManagerFactoryRef = "secondaryEntityManager",
        transactionManagerRef = "secondaryTransactionManager"
)
@Profile("!tc")
public class SecondaryDbConfiguration
{
        @Autowired
        private Environment env;

        public SecondaryDbConfiguration(){super();}
        @Bean
        @Primary
        public LocalContainerEntityManagerFactoryBean secondaryEntityManager() {
            final LocalContainerEntityManagerFactoryBean em
                    = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(secondaryDataSource());
            em.setPackagesToScan(
                    new String[] { "com.koshti.titaniam.models" });

            final HibernateJpaVendorAdapter vendorAdapter
                    = new HibernateJpaVendorAdapter();
            em.setJpaVendorAdapter(vendorAdapter);
            HashMap<String, Object> properties = new HashMap<>();
            properties.put("spring.jpa.database-platform", env.getProperty("spring.jpa.database-platform"));
            properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
            em.setJpaPropertyMap(properties);

            return em;
        }

        @Primary
        @Bean
        public DataSource secondaryDataSource() {

            DriverManagerDataSource dataSource
                    = new DriverManagerDataSource();
            dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
            dataSource.setUrl(env.getProperty("secondary.jdbc.url"));

            return dataSource;
        }

        @Primary
        @Bean
        public PlatformTransactionManager secondaryTransactionManager() {

            JpaTransactionManager transactionManager
                    = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(
                    secondaryEntityManager().getObject());
            return transactionManager;
        }
}