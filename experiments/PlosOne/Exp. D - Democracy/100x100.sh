#!/bin/sh
set -xv

if [ -d "./workspace/100x100/" ]; then
  echo "The /worspace/100x100/ directory exists. Please remove (or backup before continuing)"
  exit 0
fi


java -Xss100m -Xms4g -Xmx4g -jar ../../../executables/culsim.jar -ef 100x100.csv -id 100x100
