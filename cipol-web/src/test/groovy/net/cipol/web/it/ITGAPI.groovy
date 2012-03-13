package net.cipol.web.it

import static org.junit.Assert.assertEquals

import groovyx.net.http.HTTPBuilder;

import org.junit.Test;

class ITGAPI {
	
	@Test
	void version() {
		def http = new HTTPBuilder('http://localhost:8080/cipol/api/')
		assertEquals("", "");
	}	

}
