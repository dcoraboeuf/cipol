package net.cipol.core.db;

import javax.sql.DataSource;

import net.cipol.api.HomeService;
import net.sf.dbinit.DBInit;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {
	
	
	@Autowired
	private HomeService homeService;
	
	@Bean
	public DataSource dataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl(String.format("jdbc:h2:file:%s/db/data;AUTOCOMMIT=OFF", homeService.getHome().getAbsolutePath()));
		ds.setUsername("sa");
		ds.setPassword("");
		ds.setDefaultAutoCommit(false);
		ds.setInitialSize(5);
		ds.setMaxActive(10);
		return ds;
	}
	
	@Bean
	public DBInit dbInit() {
		DBInit db = new DBInit();
		db.setVersion(0);
		db.setJdbcDataSource(dataSource());
		db.setVersionTable("VERSION");
		db.setVersionColumnName("VALUE");
		db.setVersionColumnTimestamp("UPDATED");
		db.setResourceInitialization("/META-INF/db/init.sql");
		db.setResourceUpdate("/META-INF/db/update.{0}.sql");
		return db;
	}

}
