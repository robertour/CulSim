#!/bin/sh


if [ -d "./workspace/test_base/" ]; then
  echo "The /worspace/test_base/ directory exists. Please remove (or backup before continuing)"
  exit 0
fi


./1_from_csv.sh test_base.csv test_base 

iter=100
./3_batch_from_dir.sh test_base $iter
