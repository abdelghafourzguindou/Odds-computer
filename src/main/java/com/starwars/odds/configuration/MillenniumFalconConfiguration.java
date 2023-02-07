package com.starwars.odds.configuration;

import com.starwars.odds.model.dto.MillenniumFalconDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(
        value = "classpath:millennium-falcon.json",
        factory = JsonPropertySourceFactory.class)
@ConfigurationProperties
@Profile("web")
public class MillenniumFalconConfiguration extends MillenniumFalconDto {

}
