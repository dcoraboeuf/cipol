package net.cipol.core.support;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;

@Configuration
public class ConversionConfig {
	
	@Bean
	FactoryBean<ConversionService> conversionService() {
		return new ConversionServiceFactoryBean();
	}

}
