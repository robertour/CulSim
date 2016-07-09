#!/bin/sh
set -xv

if [ -d "./workspace/32x32/" ]; then
  echo "The /worspace/32x32/ directory exists. Please remove (or backup before continuing)"
  exit 0
fi


java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -ef 32x32.csv -id 32x32
