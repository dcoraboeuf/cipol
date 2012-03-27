import sys
import os

import cipol_env
from cipol_lib import cmd
from cipol_lib import validate
from cipol_svn import SVNLOOK

# FIXME Uses a debug() method

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

	# Logging
	sys.stderr.write ("host = %s\npath = %s\npolicy = %s\n" % (cipol_env.CIPOL_HOST, cipol_env.CIPOL_PATH, cipol_env.CIPOL_POLICY));
	
	# Message
	message  = cmd("%s log %s -t %s" % (SVNLOOK, repos, txn))
	sys.stderr.write ("message = %s\n" % message)
	
	# Author
	author = cmd("%s author %s -t %s" % (SVNLOOK, repos, txn))
	sys.stderr.write ("author = %s\n" % author)
	
	# Paths
	paths = getCommittedPaths (repos, txn)
	sys.stderr.write ("paths = %s\n" % paths)
	
	# Gets the validation message
	validation = validate (message, author, paths)

	# Not empty message => NOK
	if (validation != ""):
		sys.stderr.write ("Validation message: %s" % validation)
		sys.exit(1)
	else:
		sys.exit(0)

if __name__ == '__main__':
	if len(sys.argv) < 3:
		sys.stderr.write("Usage: %s REPOS TXN\n" % (sys.argv[0]))
	else:
		main(sys.argv[1], sys.argv[2])
