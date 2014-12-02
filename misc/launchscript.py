#!/usr/bin/env python

#
#    Creates and uploads script to be used by the nodes
#


import subprocess
import threading

SCRIPT = 'run.sh'
RUN = ("../../jdk1.8.0_25/bin/java -cp bin:lib/*"
     " ist/p2p/Main {args}")
COMPILE = ("../../jdk1.8.0_25/bin/javac -d bin/"
         " -cp .:lib/* `find src/ -name *.java`")


def cli_thread(node_addr):
    transfer_cmd = "scp {script} istple_seprs6@{location}:"
    subprocess.call(transfer_cmd.format(location=node_addr.strip(), script=SCRIPT), shell=True)


nodes = None

master = raw_input("Master address>>")

with open(SCRIPT, 'w+') as f:
    f.write('cd proj/P2PBay/\n')
    f.write(COMPILE + '\n')
    f.write(RUN.format(args='-i {ip}:{port}'.format(ip=master, port='1024')))

with open('nodes.txt', 'r') as f:
    nodes = f.readlines()

for node_addr in nodes:
    threading.Thread(target=cli_thread, args=(node_addr.strip(),)).start()
