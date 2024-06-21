package com.bittokazi.oauth2.auth.server.database;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * This class allows to use tenant specific DataSource connection And creates
 * "tenantEntityManager" and "tenantTransactionManager" bean using JPA
 * properties.
 *
 * @author Bitto Kazi
 */
@Configuration
@ComponentScan("nl.weallride.database")
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(entityManagerFactoryRef = "tenantEntityManager", transactionManagerRef = "tenantTransactionManager", basePackages = {
        "com.bittokazi.oauth2.auth.server.app.repositories.tenant" })
@EnableTransactionManagement
public class MultiTenancyJpaConfiguration {
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    /**
     * Bean Creation for "tenantEntityManager" which is required for tenant specific
     * connection Entity manager factory bean will be returned
     *
     * @param dataSource
     * @param connectionProvider
     * @param tenantResolver
     * @return emfBean
     */
    @Bean(name = "tenantEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                       MultiTenantConnectionProvider connectionProvider, CurrentTenantIdentifierResolver tenantResolver) {

        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(dataSource);
        emfBean.setPackagesToScan("com.bittokazi.oauth2.auth.server.app.models.tenant");
        emfBean.setJpaVendorAdapter(jpaVendorAdapter());

        Map<String, Object> properties = new HashMap<>();
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        properties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");

        emfBean.setJpaPropertyMap(properties);
        return emfBean;
    }

    /**
     * Bean Creation for "tenantTransactionManager" which is required for tenant
     * specific connection
     *
     * @param tenantEntityManager
     * @return
     */
    @Bean(name = "tenantTransactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory tenantEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager);
        return transactionManager;
    }
}

