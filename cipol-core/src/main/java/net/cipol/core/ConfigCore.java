package net.cipol.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import net.cipol.api.ConfigService;
import net.cipol.model.ParamValue;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigCore extends AbstractDaoService implements ConfigService {

	@Autowired
	public ConfigCore(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	@Transactional(readOnly = true)
	public <T> T loadConfig(Class<T> actualType, String reference) {
		// TODO Uses external mappers that use a named-parameter JDBC template
		final NamedParameterJdbcTemplate t = getNamedParameterJdbcTemplate();
		// Category
		String category = actualType.getName();
		// Loads the instance (just checking the existence)
		t.queryForMap(SQL.INSTANCE_FIND_BY_CATEGORY_AND_REFERENCE, 
								new MapSqlParameterSource()
									.addValue("category", category)
									.addValue("reference", reference));
		// Loads the parameters
		// TODO Uses the mapper for parameters
		List<ParamValue> params = t.query (
				SQL.PARAM_FIND_BY_CATEGORY_AND_REFERENCE,
				new MapSqlParameterSource()
					.addValue("category", category)
					.addValue("reference", reference),
				new RowMapper<ParamValue>() {
					@Override
					public ParamValue mapRow(
							ResultSet rs,
							int i)
							throws SQLException {
						ParamValue o = new ParamValue();
						o.setName(rs.getString("name"));
						o.setValue(rs.getString("value"));
						return o;
					}
				});
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
