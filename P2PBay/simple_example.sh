gnome-terminal -e 'java -cp .:../lib/* net/tomp2p/examples/ExampleSimple 0 xpto ISTO_ESTA_A_FUNCIONAR' &
gnome-terminal -e 'java -cp .:../lib/* net/tomp2p/examples/ExampleSimple 1 xpto2 YAW_YAW' &
sleep 5
gnome-terminal -e 'java -cp .:../lib/* net/tomp2p/examples/ExampleSimple 2 xpto' &
gnome-terminal -e 'java -cp .:../lib/* net/tomp2p/examples/ExampleSimple 3 xpto2' &

