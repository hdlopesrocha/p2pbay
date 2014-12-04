import subprocess
import os
import socket
import time
from threading import Thread
from time import sleep
from random import randint

node_list = [line.strip() for line in open("nodes.txt")]
bootIp =  addr = socket.gethostbyname(node_list[0])
bootPort = str(randint(1024,60000)) 

print "boot to " + bootIp+":"+bootPort

def threaded_function(index,hostname):
    if index==0:
        java_cmd = "java -jar p2pbay.jar -b " + bootPort +" -u users.txt"
    else :
        java_cmd = "java -jar p2pbay.jar -i "+bootIp+":"+bootPort
    cmd = "gnome-terminal -e 'ssh -i .ssh/id_rsa -t istple_seprs6@"+hostname+ " \"wget -N web.ist.utl.pt/ist168621/setup.sh; sh setup.sh;"+java_cmd+" ;bash\"'"
    os.system(cmd)

index = 0
for line in node_list:
    thread = Thread(target = threaded_function, args = (index,line, ))
    thread.start()
    if index==0:
        time.sleep(15)
    index = index +1
    
