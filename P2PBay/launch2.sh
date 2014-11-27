cd bin
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -u ../users.txt' & 
sleep 6
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i 127.0.0.1:1024' &
sleep 6
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i 127.0.0.1:1024' &
sleep 6
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i 127.0.0.1:1024' &
sleep 6
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i 127.0.0.1:1024' &
sleep 6
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i 127.0.0.1:1024' &
cd ..
