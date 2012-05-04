package net.cipol.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.cipol.api.ConfigService;
import net.cipol.core.test.Config1;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigCoreTest extends AbstractIntegrationTest {
	
	@Autowired
	private ConfigService configService;
	
	@Test
	public void load() {
		// Load
		Config1 config = configService.loadConfig(Config1.class, "1");
		// Check
		assertNotNull(config);
		assertEquals("My name", config.getName());
	}

}
