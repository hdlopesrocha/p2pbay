cd bin
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -u ../users.txt' & 
sleep 3
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i localhost:1024' &
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i localhost:1024' &
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i localhost:1024' &
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i localhost:1024' &
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i localhost:1024' &
cd ..
