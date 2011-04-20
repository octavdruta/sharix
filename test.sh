#/usr/bin/env bash

ant compile
java -cp .:build:lib:lib/log4j-1.2.16.jar src.Sharix andrei &
java -cp .:build:lib:lib/log4j-1.2.16.jar src.Sharix vlad &
java -cp .:build:lib:lib/log4j-1.2.16.jar src.Sharix ion &
