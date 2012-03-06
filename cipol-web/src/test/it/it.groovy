import groovyx.net.http.*

def http = new HTTPBuilder('http://localhost:8080/cipol/api/')

http.get ( path: 'version' ) { resp, json ->
	println resp.status
}