package net.cipol.web.it

import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail

import groovyx.net.http.HTTPBuilder;

import org.junit.Before;
import org.junit.Test;

class ITGAPI {
	
	def version
	
	@Before
	void before() {
		def props = new Properties()
		getClass().getResourceAsStream("/Project.properties").withStream {
			stream -> props.load(stream)
		}
		version = props["project.version"]
		println("Version = $version")
	}
	
	@Test
	void version() {
		def http = new HTTPBuilder('http://localhost:8080/cipol/api/')
		http.get ( path: 'version' ) { resp, json ->
			println("Response status : $resp.status")
			println("Response content: $json")
			def actualVersion = json.versionNumber
			if (actualVersion != version) {
				fail("Expected version $version but was $actualVersion")
			}
		}
	}	

}
