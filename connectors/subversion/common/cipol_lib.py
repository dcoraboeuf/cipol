import os

# Execute a command and gets its output
def cmd (cmd):
	output = os.popen(cmd, 'rt').read()
	return output.strip()
	