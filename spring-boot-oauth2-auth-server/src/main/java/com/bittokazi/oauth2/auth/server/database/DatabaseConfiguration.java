package com.bittokazi.oauth2.auth.server.database;

import com.bittokazi.oauth2.auth.server.utils.Utils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Bitto Kazi
 */

@Configuration
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(entityManagerFactoryRef = "masterEntityManager",
        transactionManagerRef = "masterTransactionManager",
        basePackages = {"com.bittokazi.oauth2.auth.server.app.repositories.master"})
@EnableTransactionManagement
public class DatabaseConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Value("${liquibase.context}")
    private String liquibaseContext;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.port}")
    private String port;

    @Value("${spring.datasource.databaseName}")
    private String databaseName;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.dataSourceClassName}")
    private String dataSourceClassName;

    private boolean dataBaseExist;

    public DatabaseConfiguration(JpaProperties jpaProperties) {
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariConfig config = Utils.getDbConfig(dataSourceClassName, url, port, "postgres", user, password);

        try {
            logger.info("Configuring datasource {} {} {}", dataSourceClassName, url, user);
            config.setMinimumIdle(0);
            HikariDataSource ds = new HikariDataSource(config);
            try {
                String dbExist = "SELECT datname FROM pg_catalog.pg_database WHERE lower(datname) = lower('"
                        + databaseName + "')";
                PreparedStatement statementdbExist = ds.getConnection().prepareStatement(dbExist);
                if (statementdbExist.executeQuery().next()) {
                    statementdbExist.close();
                    ds.close();
                    this.dataBaseExist = true;
                } else {
                    statementdbExist.close();
                    String sql = "CREATE DATABASE " + databaseName;
                    PreparedStatement statement = ds.getConnection().prepareStatement(sql);
                    statement.executeUpdate();
                    statement.close();
                    ds.close();
                }
            } catch (SQLException e) {
                logger.error("ERROR Configuring datasource SqlException {} {} {}", e);
                this.dataBaseExist = true;
            } catch (Exception ee) {
                this.dataBaseExist = true;
                logger.debug("ERROR Configuring datasource catch ", ee);
            }
            HikariConfig config2 = Utils.getDbConfig(dataSourceClassName, url, port, databaseName, user, password);
            return new HikariDataSource(config2);
        } catch (Exception e) {
            logger.error("ERROR in database configuration ", e);
        } finally {

        }
        return null;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase sl = new SpringLiquibase();
        sl.setDataSource(dataSource);
        sl.setContexts(liquibaseContext);
        if (this.dataBaseExist) {
            sl.setChangeLog("classpath:/db-migration/db-master-alter.xml");
        } else {
            sl.setChangeLog("classpath:/db-migration/db-master.xml");
        }
        sl.setShouldRun(true);
        return sl;
    }

    @Bean(name = "masterEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.bittokazi.oauth2.auth.server.app.models.master");
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(new Properties());
        em.setPersistenceUnitName("master");
        return em;
    }

    @Bean(name = "masterTransactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory masterEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(masterEntityManager);
        return transactionManager;
    }

}
