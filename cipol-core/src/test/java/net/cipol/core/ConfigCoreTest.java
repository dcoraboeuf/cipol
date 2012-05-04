package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.cipol.api.ConfigService;
import net.cipol.core.test.Config1;
import net.cipol.core.test.Config2;

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

}
