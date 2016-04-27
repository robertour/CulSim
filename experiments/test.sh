#!/bin/sh

if [ -d "./workspace/div/" ]; then
  echo "The /worspace/div/ directory exists. Please remove (or backup before continuing)"
  exit 1
fi
if [ -d "./workspace/no_div/" ]; then
  echo "The /worspace/no_div/ directory exists. Please remove (or backup before continuing)"
  exit 1
fi


./1_from_csv.sh test_div.csv div 1000
./1_from_csv.sh test_no_div.csv no_div 1000

iter=1000
./3_batch_from_dir.sh div 0.16667 1_6 $iter
./3_batch_from_dir.sh div 0.33333 2_6 $iter
./3_batch_from_dir.sh div 0.5 3_6 $iter
./3_batch_from_dir.sh div 0.66667 4_6 $iter
./3_batch_from_dir.sh div 0.83333 5_6 $iter
./3_batch_from_dir.sh no_div 0.16667 1_6 $iter
./3_batch_from_dir.sh no_div 0.33333 2_6 $iter
./3_batch_from_dir.sh no_div 0.5 3_6 $iter
./3_batch_from_dir.sh no_div 0.66667 4_6 $iter
./3_batch_from_dir.sh no_div 0.83333 5_6 $iter
