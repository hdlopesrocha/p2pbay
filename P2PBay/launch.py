import os
import time
from threading import Thread
from time import sleep

bootIp = "132.227.62.122"
bootPort = "5672"



def threaded_function(bootstrap,hostname):
    if bootstrap==hostname:
        java_cmd = "java -jar p2pbay.jar -b " + bootPort +" -u users.txt"
    else :
        java_cmd = "java -jar p2pbay.jar -i "+bootIp+":"+bootPort

    cmd = "gnome-terminal -e 'ssh -i .ssh/id_rsa -t istple_seprs6@"+hostname+ " \"wget -N web.ist.utl.pt/ist168621/setup.sh; sh setup.sh;"+java_cmd+" ;bash\"'"
    os.system(cmd)
    print cmd
node_list = [line.strip() for line in open("nodes.txt")]

for line in node_list:
    thread = Thread(target = threaded_function, args = (node_list[0],line, ))
    thread.start()
    if node_list[0]==line:
        time.sleep(15)
    #thread.join()
    
