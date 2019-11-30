#!/bin/bash
# first step is to compile IDL which generates stub for server
ulimit -v 4194304
idlj -J-Xmx16m -fall AuthFIT.idl
#second step is compile client and then server program
javac AuthClient.java
javac AuthServer.java
#next step: run all the stuff :)
#we generate random port for session
port=$RANDOM ; while [ $port -le 1024 ]; do port=$RANDOM ; done; echo $port
#first we need to run Transient Naming Service
tnameserv -J-Xmx16m -J-Djava.net.preferIPv4Stack=true -ORBInitialPort $port &
sleep 10
#second run server on background
java -Djava.net.preferIPv4Stack=true AuthServer -ORBInitialPort $port &
sleep 10
#last run client
java -Djava.net.preferIPv4Stack=true AuthClient -ORBInitialPort $port
kill %2
kill %1
