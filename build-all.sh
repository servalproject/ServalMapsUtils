#!/bin/bash
#
# a script to build all of the utilities
for i in $(find . -type d -maxdepth 1 -not -name ".*")
do
	cd $i
	ant
	cd ../
done
