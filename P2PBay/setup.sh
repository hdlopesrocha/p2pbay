# THIS SCRIPT SHOULD BE AVAILABLE ON "web.ist.utl.pt/ist168621/setup.sh"

if [ -d "/opt/jdk/" ]; then
	echo 'jdk already exists!'
else
	wget -N -O java.tar.gz --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u25-b17/jdk-8u25-linux-i586.tar.gz
	sudo mkdir /opt/jdk
	sudo tar -zxf java.tar.gz -C /opt/jdk
	echo 'export PATH=/opt/jdk/jdk1.8.0_25/bin/:${PATH}' >> /home/istple_seprs6/.bashrc
	export PATH=/opt/jdk/jdk1.8.0_25/bin/:${PATH}﻿
fi

wget -N web.ist.utl.pt/ist168621/p2pbay.jar
wget -N web.ist.utl.pt/ist168621/users.txt

