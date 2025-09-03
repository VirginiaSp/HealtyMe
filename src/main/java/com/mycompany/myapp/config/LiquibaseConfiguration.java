package com.mycompany.myapp.config;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class LiquibaseConfiguration {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource, LiquibaseProperties liquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());

        if (StringUtils.hasText(liquibaseProperties.getContexts())) {
            liquibase.setContexts(liquibaseProperties.getContexts());
        }

        // Προσορινά δεν ορίζουμε labels, επειδή δεν υπάρχει getter σε αυτή την έκδοση

        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        return liquibase;
    }
}
