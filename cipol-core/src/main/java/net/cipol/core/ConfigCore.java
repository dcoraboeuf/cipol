package net.cipol.core;

import static net.cipol.core.SQLColumns.*;
import static net.cipol.core.SQLColumns.VALUE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import net.cipol.api.ConfigService;
import net.cipol.model.GeneralConfiguration;
import net.cipol.model.Instance;
import net.cipol.model.ParamValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigCore extends AbstractDaoService implements ConfigService {

	private static final Logger log = LoggerFactory.getLogger(ConfigService.class);

	private static final class ParamRowMapper implements RowMapper<ParamValue> {
		@Override
		public ParamValue mapRow(
				ResultSet rs,
				int i)
				throws SQLException {
			ParamValue o = new ParamValue();
			o.setName(rs.getString(NAME));
			o.setValue(rs.getString(VALUE));
			return o;
		}
	}

	@Autowired
	public ConfigCore(DataSource dataSource) {
		super(dataSource);
	}
	
	protected MapSqlParameterSource categoryReference (String category, String reference) {
		return new MapSqlParameterSource()
			.addValue(CATEGORY, category)
			.addValue(REFERENCE, reference);
	}
	
	@Override
	@Transactional(readOnly = true)
	public String loadGeneralParameter(String name, boolean required, String defaultValue) {
		try {
			return loadParameter(GeneralConfiguration.class, "0", name, required, defaultValue);
		} catch (ParameterRequiredException ex) {
			throw new GeneralParameterRequiredException(name);
		}
	}
	
	@Override
	@Transactional
	public void saveGeneralParameter(String name, String value) {
		saveParameter(GeneralConfiguration.class, "0", name, value);
	}

	@Override
	@Transactional(readOnly = true)
	public void saveParameter(Class<?> type, String reference, String name, String value) {
		final NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		// Category
		String category = type.getName();
		log.debug("Saving parameter {}@{} {}={}", new Object[]{category, reference, name, value});
		MapSqlParameterSource paramsSqlSource = categoryReference(category, reference).addValue("name", name).addValue("value", value);
		// Checks the instance is created
		try {
			t.queryForObject (SQL.INSTANCE_FIND_BY_CATEGORY_AND_REFERENCE,
					paramsSqlSource,
					new RowMapper<Instance>() {
						@Override
						public Instance mapRow(ResultSet rs, int i)
								throws SQLException {
							Instance o = new Instance();
							o.setCategory(rs.getString(CATEGORY));
							o.setReference(rs.getString(REFERENCE));
							return o;
						}});
		} catch (EmptyResultDataAccessException ex) {
			// We need to create the instance
			t.update(SQL.INSTANCE_CREATE, paramsSqlSource);
		}
		// Clears any existing parameter first
		t.update (SQL.PARAM_REMOVE, paramsSqlSource);
		// Saves the parameter
		t.update (SQL.PARAM_INSERT, paramsSqlSource);		
	}
	
	@Override
	@Transactional(readOnly = true)
	public String loadParameter(Class<?> type, String reference, String name,
			boolean required, String defaultValue) {
		final NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		// Category
		String category = type.getName();
		// Query
		try {
			ParamValue paramValue = t.queryForObject (SQL.PARAM_FIND_BY_CATEGORY_AND_REFERENCE, 
					categoryReference(category, reference), new ParamRowMapper());
			return paramValue.getValue();
		} catch (EmptyResultDataAccessException ex) {
			if (required) {
				throw new ParameterRequiredException (category, reference, name);
			} else {
				return defaultValue;
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public <T> T loadConfig(Class<T> actualType, String reference) {
		// TODO Uses external mappers that use a named-parameter JDBC template
		final NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		// Category
		String category = actualType.getName();
		// Loads the instance (just checking the existence)
		try {
			t.queryForMap(SQL.INSTANCE_FIND_BY_CATEGORY_AND_REFERENCE, categoryReference(category, reference));
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
		// Loads the parameters
		// TODO Uses the mapper for parameters
		List<ParamValue> params = t.query (
				SQL.PARAM_FIND_BY_CATEGORY_AND_REFERENCE,
				categoryReference(category, reference),
				new ParamRowMapper());
		// Creates the instance
		BeanWrapper wrapper = new BeanWrapperImpl(actualType);
		for (ParamValue paramValue : params) {
			String pName = paramValue.getName();
			String pValue = paramValue.getValue();
			wrapper.setPropertyValue(pName, pValue);
		}
		// OK
		@SuppressWarnings("unchecked")
		T result = (T) wrapper.getWrappedInstance();
		return result;
	}

}
