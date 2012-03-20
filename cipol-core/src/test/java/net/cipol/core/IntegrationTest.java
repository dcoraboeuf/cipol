package net.cipol.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import net.cipol.api.APIService;
import net.cipol.api.HomeService;
import net.cipol.model.CommitInformation;
import net.cipol.model.ValidationReport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/*.xml" })
@ActiveProfiles(profiles = { "test" })
public class IntegrationTest {

	@Autowired
	private APIService api;

	@Autowired
	private HomeService homeService;

	@Test
	public void check() {
		// Configuration OK
		assertNotNull(api);
		assertNotNull(homeService);
	}

	@Test
	public void home() {
		File file = homeService.getFile("net.cipol.model.Policy.bare.json");
		assertNotNull(file);
		assertTrue(file.exists());
	}
	
	@Test(expected = PolicyNotFoundException.class)
	public void validate_no_policy() {
		CommitInformation ci = new CommitInformation();
		api.validate("notfound", ci);
	}
	
	@Test
	public void validate_bare() {
		CommitInformation ci = new CommitInformation();
		ValidationReport report = api.validate("bare", ci);
		assertNotNull(report);
		assertTrue(report.isSuccess());
		assertNull(report.getMessage());
	}

}
