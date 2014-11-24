rm `find bin -name *.class`
javac -d bin `find src -name *.java` 
gnome-terminal -e 'java -cp bin/ seprs.unstructured.Servent 1  ' &
sleep 1
gnome-terminal -e 'java -cp bin/ seprs.unstructured.Servent 2 ' &
sleep 1
gnome-terminal -e 'java -cp bin/ seprs.unstructured.Servent 3 ' &
#sleep 1
#gnome-terminal -e 'java -cp bin/ seprs.unstructured.Servent 4 ' &
#sleep 1
#gnome-terminal -e 'java -cp bin/ seprs.unstructured.Servent 5 ' &
#sleep 1
#gnome-terminal -e 'java -cp bin/ seprs.unstructured.Servent 6 ' &
#sleep 1
#gnome-terminal -e 'java -cp bin/ seprs.unstructured.Servent 7 ' &
#sleep 1
#gnome-terminal -e 'java -cp bin/ seprs.unstructured.Servent 8 ' &
#sleep 1
#gnome-terminal -e 'java -cp bin/ seprs.unstructured.Servent 9 ' &
#sleep 1
#gnome-terminal -e 'java -cp bin/ seprs.unstructured.Servent 10 ' &

