#!/bin/sh
set -xv

if [ -d "./workspace/10x10/" ]; then
  echo "The /worspace/10x10/ directory exists. Please remove (or backup before continuing)"
  exit 0
fi


java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -ef 10x10.csv -id 10x10
