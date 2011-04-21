#/usr/bin/env bash

ant compile
sleep 2
java -cp .:build:lib:lib/log4j-1.2.16.jar src.Sharix andrei &
sleep 2
java -cp .:build:lib:lib/log4j-1.2.16.jar src.Sharix vlad &
sleep 2
java -cp .:build:lib:lib/log4j-1.2.16.jar src.Sharix ion &
sleep 2
