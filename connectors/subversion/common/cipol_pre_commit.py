import sys
import os

import cipol_env
from cipol_lib import cmd
from cipol_svn import SVNLOOK

import json
import httplib

# Gets the list of committed paths for a transaction, without regard
# to the action
# @param repos Path to the repository
# @param txn Commit transaction
# @return A list of paths
def getCommittedPaths (repos, txn):
	# Executes the 'changed' command to get the list of paths
	result = cmd ("%s changed %s -t %s" % (SVNLOOK, repos, txn))
	# Gets all the paths
	changedPaths = result.splitlines()
	# Clean-up of paths
	paths = [path[4:] for path in changedPaths]
	# Test
	return paths

def main(repos, txn):

	sys.stderr.write ("host = %s\npath = %s\npolicy = %s\n" % (cipol_env.CIPOL_HOST, cipol_env.CIPOL_PATH, cipol_env.CIPOL_POLICY));
	
	message  = cmd("%s log %s -t %s" % (SVNLOOK, repos, txn))
	sys.stderr.write ("message = %s\n" % message)
	
	author = cmd("%s author %s -t %s" % (SVNLOOK, repos, txn))
	sys.stderr.write ("author = %s\n" % author)
	
	paths = getCommittedPaths (repos, txn)
	sys.stderr.write ("paths = %s\n" % paths)

	sys.exit(1)
	
	conn = httplib.HTTPConnection(cipol_env.CIPOL_HOST)
	conn.request("GET", "%s/api/version" % (cipol_env.CIPOL_PATH))
	resp = conn.getresponse()
	data = resp.read()
	sys.stderr.write ("status = %s, reason = %s\ndata = %s\n" % (resp.status, resp.reason, data))
	o = json.loads(data)
	sys.stderr.write ("version = %s\n" % (o['versionNumber']))

if __name__ == '__main__':
	if len(sys.argv) < 3:
		sys.stderr.write("Usage: %s REPOS TXN\n" % (sys.argv[0]))
	else:
		main(sys.argv[1], sys.argv[2])
