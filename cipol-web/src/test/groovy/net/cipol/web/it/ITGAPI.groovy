package net.cipol.web.it

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail

import groovyx.net.http.ContentType;
import groovyx.net.http.HTTPBuilder;
import groovyx.net.http.Method;

import org.junit.Before;
import org.junit.Test;

class ITGAPI {
	
	def version
	def http
	
	@Before
	void before() {
		def itPort = System.properties['it.port']
		println ("it.port = $itPort")
		if (itPort == null || itPort == "") {
			itPort = "8080"
		}
		def props = new Properties()
		getClass().getResourceAsStream("/Project.properties").withStream {
			stream -> props.load(stream)
		}
		version = props["project.version"]
		println("Version = $version")
		// URL of the server
		def url = "http://localhost:$itPort/cipol/api/"
		println ("URL = $url")
		// Creates the HTTP client for the API
		http = new HTTPBuilder(url)
	}
	
	@Test
	void version() {
		http.get ( path: 'version' ) { resp, json ->
			println("Response status : $resp.status")
			println("Response content: $json")
			def actualVersion = json.versionNumber
			if (actualVersion != version) {
				fail("Expected version $version but was $actualVersion")
			}
		}
	}
	
	@Test
	void validate_no_policy() {
		http.request ( Method.POST, ContentType.JSON ) {
			uri.path = 'validate/UID'
			body = [author: 'albert', message: 'My message']
			requestContentType = ContentType.JSON
			response.success = { resp, json ->
				println("Response status : $resp.status")
				println("Response content: $json")
				assertFalse(json.valid);
				assertEquals(1, json.messages.size());
				assertTrue(json.messages[0].startsWith("[CORE-004]"));				
			}
		}
	}	
	
	@Test
	void validate_no_rule() {
		http.request ( Method.POST, ContentType.JSON ) {
			uri.path = 'validate/9853bf50-6d1d-11e1-b0c4-0800200c9a66'
			body = [author: 'albert', message: 'My message']
			requestContentType = ContentType.JSON
			response.success = { resp, json ->
				println("Response status : $resp.status")
				println("Response content: $json")
				assertTrue(json.valid);
				assertEquals(0, json.messages.size());				
			}
		}
	}	

}
