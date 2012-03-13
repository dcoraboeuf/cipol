package net.cipol.web.api.controller;

import static org.junit.Assert.assertSame;
import net.cipol.api.APIService;
import net.cipol.api.model.VersionInformation;
import net.sf.jstring.Strings;

import org.junit.Test;
import org.mockito.Mockito;

public class APIControllerTest {

	@Test
	public void version() {
		// Mock
		APIService service = Mockito.mock(APIService.class);
		VersionInformation expectedVersionInformation = new VersionInformation();
		Mockito.when(service.getVersionInformation()).thenReturn(
				expectedVersionInformation);
		Strings strings = new Strings();
		// Call
		APIController controller = new APIController(service, strings);
		VersionInformation versionInformation = controller
				.getVersionInformation();
		// Checks
		assertSame(expectedVersionInformation, versionInformation);
	}

}
