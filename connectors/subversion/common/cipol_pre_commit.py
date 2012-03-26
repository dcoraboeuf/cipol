import sys
import os

import cipol_env

def main(repos, txn):

	sys.stderr.write ("url = %s\npolicy = %s\n" % (cipol_env.CIPOL_URL, cipol_env.CIPOL_POLICY));
	sys.exit(1)

if __name__ == '__main__':
	if len(sys.argv) < 3:
		sys.stderr.write("Usage: %s REPOS TXN\n" % (sys.argv[0]))
	else:
		main(sys.argv[1], sys.argv[2])
