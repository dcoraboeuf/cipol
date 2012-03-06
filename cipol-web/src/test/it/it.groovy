import groovyx.net.http.*

def expectedVersion = "$project.version"
def http = new HTTPBuilder('http://localhost:8080/cipol/api/')

http.get ( path: 'version' ) { resp, json ->
	log.info("Response status : {}", resp.status)
	log.info("Response content: {}", json)
	def actualVersion = json.versionNumber
	if (actualVersion != expectedVersion) {
		fail("Expected version $expectedVersion but was $actualVersion")
	}
}