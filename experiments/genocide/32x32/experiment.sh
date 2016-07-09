#!/bin/sh

# this is commented because it failed
#if [ -d "./workspace/base/" ]; then
#  echo "The /worspace/base/ directory exists. Please remove (or backup before continuing)"
#  exit 0
#fi


#./1_from_csv.sh base.csv base 

iter=100000
./3_batch_from_dir.sh base $iter "genocide32"
