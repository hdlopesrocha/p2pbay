Dim oShell1, oShell2, oShell3
Set oShell1 = WScript.CreateObject ("WScript.Shell")
Set oShell2 = WScript.CreateObject ("WScript.Shell")
Set oShell3 = WScript.CreateObject ("WScript.Shell")
return = oShell1.run("cmd /K CD bin & java -jar p2pbay.jar -b 1024 -u ../users.txt", 1, false)
WScript.Sleep 5000
return = oShell2.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024",6, false)
return = oShell3.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024 -s",6, false)
Set oShell1 = Nothing
Set oShell2 = Nothing
Set oShell3 = Nothing