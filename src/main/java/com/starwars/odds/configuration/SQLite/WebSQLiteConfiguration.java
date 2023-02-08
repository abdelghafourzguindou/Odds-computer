package com.starwars.odds.configuration.SQLite;

import com.starwars.odds.configuration.MillenniumFalconConfiguration;
import com.starwars.odds.utils.DataLoadingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@Profile("web")
public class WebSQLiteConfiguration {

    private final MillenniumFalconConfiguration millenniumFalconConfiguration;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() throws FileNotFoundException {
        var dataSource = new DriverManagerDataSource();
        var absolutePath = millenniumFalconConfiguration.getRoutesDb();
        var relativePath = Objects
                .requireNonNull(this.getClass()
                        .getClassLoader()
                        .getResource(millenniumFalconConfiguration.getRoutesDb()))
                .getPath();

        dataSource.setDriverClassName(driverClassName);

        DataLoadingUtils.setUrl(dataSource, absolutePath, relativePath, millenniumFalconConfiguration.getRoutesDb());

        return dataSource;
    }
}