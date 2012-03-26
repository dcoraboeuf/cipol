import sys
import os

import cipol_env

import json
import httplib

def main(repos, txn):

	sys.stderr.write ("host = %s\npath = %s\npolicy = %s\n" % (cipol_env.CIPOL_HOST, cipol_env.CIPOL_PATH, cipol_env.CIPOL_POLICY));
	
	conn = httplib.HTTPConnection(cipol_env.CIPOL_HOST)
	conn.request("GET", "%s/api/version" % (cipol_env.CIPOL_PATH))
	resp = conn.getresponse()
	data = resp.read()
	sys.stderr.write ("status = %s, reason = %s\ndata = %s\n" % (resp.status, resp.reason, data))
	o = json.loads(data)
	sys.stderr.write ("version = %s\n" % (o['versionNumber']))

	sys.exit(1)

if __name__ == '__main__':
	if len(sys.argv) < 3:
		sys.stderr.write("Usage: %s REPOS TXN\n" % (sys.argv[0]))
	else:
		main(sys.argv[1], sys.argv[2])
