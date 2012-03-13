package net.cipol.core;

import static org.junit.Assert.assertNotNull;
import net.cipol.api.APIService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/*.xml"})
public class IntegrationTest {
	
	@Autowired
	private APIService api; 
	
	@Test
	public void check() {
		// Configuration OK
		assertNotNull(api);
	}

}
