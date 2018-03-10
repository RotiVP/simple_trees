#! /usr/bin/env bash

# simpletrees

ELEMENTS=(model/FibTree example/CliTester)
PROJECTPATH=src/rot/simpletrees

for ELEMENT in ${ELEMENTS[@]}; do
	javac -Xlint:unchecked -d bin -sourcepath src -classpath lib ${PROJECTPATH}/${ELEMENT}.java
done 2>&1 | less
