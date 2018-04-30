#! /usr/bin/env bash

#while [[ -n $1 ]]; do
#	case $1 in
#		GuiTester)
#			;;
#		*) 
#			;;
#	esac
#	shift
#done

PROGS=(clitester/CliTester guitester/GuiTester)
PROJECTPATH=src/rot/simpletrees

# 
for PROG in ${PROGS[@]}; do
	javac -g -Xlint:unchecked -d bin -sourcepath src -classpath lib ${PROJECTPATH}/${PROG}.java
done 2>&1 | less
