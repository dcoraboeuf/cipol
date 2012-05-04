package net.cipol.core.db;

import javax.sql.DataSource;

import net.cipol.CipolProfiles;
import net.cipol.api.HomeService;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({ CipolProfiles.PROD, CipolProfiles.DEV })
public class DataSourceConfig {
	
	
	@Autowired
	private HomeService homeService;
	
	@Bean
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl(String.format("jdbc:h2:file:%s/db/data;AUTOCOMMIT=OFF;MVCC=true", homeService.getHome().getAbsolutePath()));
		ds.setUsername("sa");
		ds.setPassword("");
		ds.setDefaultAutoCommit(false);
		ds.setInitialSize(5);
		ds.setMaxActive(10);
		return ds;
	}

}
