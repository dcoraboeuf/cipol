package net.cipol.core;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public abstract class AbstractDaoService extends NamedParameterJdbcDaoSupport {
	
	public AbstractDaoService(DataSource dataSource) {
		setDataSource(dataSource);
	}

}
