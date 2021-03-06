package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import net.cipol.api.ConfigService;
import net.cipol.core.test.Config1;
import net.cipol.core.test.Config2;
import net.cipol.core.test.Config3;
import net.cipol.model.GeneralConfiguration;
import net.cipol.test.AbstractIntegrationTest;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigCoreTest extends AbstractIntegrationTest {
	
	@Autowired
	private ConfigService configService;
	
	@Test
	public void loadNotFound() {
		Config1 config = configService.loadConfig(Config1.class, "3");
		assertNull(config);
	}
	
	@Test
	public void loadOneField_1() {
		Config1 config = configService.loadConfig(Config1.class, "1");
		assertNotNull(config);
		assertEquals("My name", config.getName());
	}
	
	@Test
	public void loadOneField_2() {
		Config1 config = configService.loadConfig(Config1.class, "2");
		assertNotNull(config);
		assertEquals("My second name", config.getName());
	}
	
	@Test
	public void loadTwoFields() {
		Config2 config = configService.loadConfig(Config2.class, "1");
		assertNotNull(config);
		assertEquals("My name", config.getName());
		assertEquals("http://github.com", config.getUrl());
	}
	
	@Test
	public void loadSeveralFields_1() {
		Config3 config = configService.loadConfig(Config3.class, "1");
		assertNotNull(config);
		assertEquals("My name", config.getName());
		assertEquals("http://github.com", config.getUrl());
		assertEquals(false, config.isDisabled());
		assertEquals(10, config.getHits());
	}
	
	@Test
	public void loadSeveralFields_2() {
		Config3 config = configService.loadConfig(Config3.class, "2");
		assertNotNull(config);
		assertEquals("My name", config.getName());
		assertEquals("http://github.com", config.getUrl());
		assertEquals(true, config.isDisabled());
		assertEquals(100, config.getHits());
	}
	
	@Test
	public void general_default_value() {
		String value = configService.loadGeneralParameter("test", false, "My default value");
		assertEquals("My default value", value);
	}
	
	@Test(expected = GeneralParameterRequiredException.class)
	public void general_required() {
		configService.loadGeneralParameter("test", true, null);
	}
	
	@Test
	public void general_load_change_reload() throws DataSetException, SQLException {
		String value = configService.loadGeneralParameter("name", false, "My name");
		assertEquals("My name", value);
		configService.saveGeneralParameter("name", "My new name");
		value = configService.loadGeneralParameter("name", false, "My name");
		assertEquals("My new name", value);
		// Checks the param table
		ITable params = getTable("PARAMS", "select * from param where category = '%s' and reference = '0' and name = 'name'", GeneralConfiguration.class.getName());
		assertEquals(1, params.getRowCount());
		assertEquals("name", params.getValue(0, "name"));
		assertEquals("My new name", params.getValue(0, "value"));
	}

}
