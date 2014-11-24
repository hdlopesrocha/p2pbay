ant
gnome-terminal -e 'java -cp bin/classes:./lib/* ist/p2p/Main -u users.txt' & 
sleep 1
gnome-terminal -e 'java -cp bin/classes:./lib/* ist/p2p/Main -i 127.0.0.1:1024' &
sleep 1
gnome-terminal -e 'java -cp bin/classes:./lib/* ist/p2p/Main -i 127.0.0.1:1024' &
sleep 1 
gnome-terminal -e 'java -cp bin/classes:./lib/* ist/p2p/Main -i 127.0.0.1:1024' &
#sleep 1
#gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i 127.0.0.1:1024' &
#sleep 1
#gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i 127.0.0.1:1024' &
sleep 1
gnome-terminal -e 'java -cp bin/classes:./lib/* ist/p2p/ManagerMain -i 127.0.0.1:1024' &

