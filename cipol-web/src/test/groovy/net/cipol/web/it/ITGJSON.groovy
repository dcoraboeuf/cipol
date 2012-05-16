package net.cipol.web.it

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail

import net.sf.json.JSONNull

import groovyx.net.http.ContentType;
import groovyx.net.http.HTTPBuilder;
import groovyx.net.http.Method;

import org.junit.Before;
import org.junit.Test;

class ITGJSON {
	
	def http
	
	@Before
	void before() {
		def itPort = System.properties['it.port']
		if (itPort == null || itPort == "") {
			itPort = "9999"
		}
		println ("it.port = $itPort")
		// URL of the server
		def url = "http://localhost:$itPort/cipol/"
		println ("URL = $url")
		// Creates the HTTP client for the API
		http = new HTTPBuilder(url)
	}
	
	void doLogin (username, password) {
		http.request ( Method.GET ) {
			uri.path = 'ui/'
					
			response.success = { resp, content ->
				println("Login response status : $resp.status")
				println("Login response content: $content")
				assertEquals (200, resp.status);
				
				// Sends login information
				http.request ( Method.POST ) {
					uri.path = 'j_spring_security_check'
					send ContentType.URLENC, [j_username: username, j_password: password]
					response.success = { respLogin, contentLogin ->
						println("Login response status : $respLogin.status")
						println("Login response content: $contentLogin")
						assertEquals (302, respLogin.status);
						// Authentication is now OK
					}
				}
				
			}
		}
	}
	
	void doLogout () {
		http.request ( Method.GET ) {
			uri.path = 'logout'
					
			response.success = { resp, content ->
				println("Logout response status : $resp.status")
				println("Logout response content: $content")
				assertEquals (200, resp.status);				
			}
		}
	}
	
	void with_session (username, password, action) {
		doLogin (username, password);
		try {
			action();
		} finally {
			doLogout();
		}
	}
	
	@Test
	void login_test() {
		def testPassed = false
		with_session ('admin', 'admin', { testPassed = true });
		assertTrue ("The test was not executed", testPassed);
	}
	
	@Test
	void group_create_ok() {
		with_session ('admin', 'admin', {
				http.request ( Method.POST, ContentType.TEXT ) {
					uri.path = 'ui/policy/group/9853bf50-6d1d-11e1-b0c4-0800200c9a66/create'
					send ContentType.URLENC, [name: 'group1']
					response.success = { resp, reader ->
						def content = reader.text
						println("Response status : $resp.status")
						println("Response content: $content")
						assertEquals (200, resp.status)
						assertEquals ("OK", content)
					}
				}
			})
	}	

}
