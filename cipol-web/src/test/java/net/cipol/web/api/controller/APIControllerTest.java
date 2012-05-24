package net.cipol.web.api.controller;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;

import net.cipol.api.APIService;
import net.cipol.model.VersionInformation;
import net.cipol.web.ui.locale.CurrentLocale;
import net.sf.jstring.Strings;

import org.junit.Test;

public class APIControllerTest {

	@Test
	public void version() {
		// Mock
		APIService service = mock(APIService.class);
		VersionInformation expectedVersionInformation = new VersionInformation();
		when(service.getVersionInformation()).thenReturn(
				expectedVersionInformation);
		Strings strings = new Strings();
		CurrentLocale currentLocale = mock (CurrentLocale.class);
		when (currentLocale.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		// Call
		APIController controller = new APIController(service, strings, currentLocale);
		VersionInformation versionInformation = controller
				.getVersionInformation();
		// Checks
		assertSame(expectedVersionInformation, versionInformation);
	}

}
