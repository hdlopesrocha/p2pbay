cd bin
gnome-terminal -e 'bash -c "java -jar p2pbay.jar -u ../users.txt -b 1024"' & 
x=1
while [ $x -lt 20 ]
do
sleep 5
gnome-terminal -e 'bash -c "java -jar p2pbay.jar -i 127.0.0.1:1024"' &
x=$(( $x + 1 ))
done
cd ..
