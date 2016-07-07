#!/bin/sh
set -xv

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
./3_batch_from_dir.sh div 0.2 1_5 $iter
./3_batch_from_dir.sh div 0.4 2_5 $iter
./3_batch_from_dir.sh div 0.6 3_5 $iter
./3_batch_from_dir.sh div 0.8 4_5 $iter
./3_batch_from_dir.sh no_div 0.2 1_5 $iter
./3_batch_from_dir.sh no_div 0.4 2_5 $iter
./3_batch_from_dir.sh no_div 0.6 3_5 $iter
./3_batch_from_dir.sh no_div 0.8 4_5 $iter

./2_evs_from_dir.sh workspace/div @U\,1.0 $iter "div" "ALL_5_5"
./2_evs_from_dir.sh workspace/no_div @U\,1.0 $iter "no_div" "ALL_5_5"
