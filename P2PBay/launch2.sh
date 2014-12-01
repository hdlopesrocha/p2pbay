cd bin
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -u ../users.txt' & 
x=1
while [ $x -le 10 ]
do
sleep 4
gnome-terminal -e 'java -cp .:../lib/* ist/p2p/Main -i 127.0.0.1:1024' &
x=$(( $x + 1 ))
done
cd ..
