Dim oShell1, oShell2, oShell3, oShell4, oShell5, oShell6, oShell7, oShell8, oShell9, oShell10
Set oShell1 = WScript.CreateObject ("WScript.Shell")
Set oShell2 = WScript.CreateObject ("WScript.Shell")
Set oShell3 = WScript.CreateObject ("WScript.Shell")
Set oShell4 = WScript.CreateObject ("WScript.Shell")
Set oShell5 = WScript.CreateObject ("WScript.Shell")
Set oShell6 = WScript.CreateObject ("WScript.Shell")
Set oShell7 = WScript.CreateObject ("WScript.Shell")
Set oShell8 = WScript.CreateObject ("WScript.Shell")
Set oShell9 = WScript.CreateObject ("WScript.Shell")
Set oShell10 = WScript.CreateObject ("WScript.Shell")
return = oShell1.run("cmd /K CD bin & java -jar p2pbay.jar -u ../users.txt", 1, false)
WScript.Sleep 5000
return = oShell2.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024",6, false)
return = oShell3.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024",6, false)
return = oShell4.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024",6, false)
return = oShell5.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024",6, false)
return = oShell6.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024",6, false)
return = oShell7.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024",6, false)
return = oShell8.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024",6, false)
return = oShell9.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024",6, false)
return = oShell10.run("cmd /K CD bin & java -jar p2pbay.jar -i localhost:1024",6, false)
Set oShell1 = Nothing
Set oShell2 = Nothing
Set oShell3 = Nothing
Set oShell4 = Nothing
Set oShell5 = Nothing
Set oShell6 = Nothing
Set oShell7 = Nothing
Set oShell8 = Nothing
Set oShell9 = Nothing
Set oShell10 = Nothing