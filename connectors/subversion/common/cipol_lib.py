import os
import sys

import cipol_env

import json
import httplib

# Execute a command and gets its output
def cmd (cmd):
	output = os.popen(cmd, 'rt').read()
	return output.strip()
	
# Validation
def validate (message, author, paths):
	# Builds the commit information
	# a CommitInformation
	# has
	# an author,
	# a message,
	# a collection of string as paths
	# .
	ci = {
		"message": message,
		"author": author,
		"paths": paths
		}
	sys.stderr.write ("ci = %s\n" % ci)
	# Sends the request
	conn = httplib.HTTPConnection(cipol_env.CIPOL_HOST)
	try:
		# Headers
		headers = {
			"Content-type": "application/json",
			"Accept": "application/json"
			}
		# Content
		data = json.dumps(ci)
		sys.stderr.write ("data = %s\n" % data)
		# Request
		conn.request("POST", "%s/api/validate/%s" % (cipol_env.CIPOL_PATH, cipol_env.CIPOL_POLICY), data, headers)
		# Gets the response
		resp = conn.getresponse()
		if (resp.status != 200):
			return "HTTP %s %s" % (resp.status, resp.reason)
		else:
			# Gets the returned data
			responseData = resp.read()
			sys.stderr.write ("responseData = %s\n" % responseData)
			# Parsing
			# a ValidationReport
			# has
			# a boolean as success,
			# a nullable string as message,
			# a collection of ValidationDetail as details
			# .
			report = json.loads(responseData)
			sys.stderr.write ("report = %s\n" % report)
			# Interprets the data
			if (report['success']):
				return None
			else:
				return report['message']
	finally:
		conn.close()
	
	# OK
	return "NOK"
	