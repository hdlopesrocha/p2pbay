cd bin
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -u ../users.txt' & 
sleep 1
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i localhost:1024' &
sleep 1
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i localhost:1024' &
sleep 1
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i localhost:1024' &
sleep 1
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i localhost:1024' &
sleep 1
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i localhost:1024' &
cd ..
