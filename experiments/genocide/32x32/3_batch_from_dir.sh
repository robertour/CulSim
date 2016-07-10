#!/bin/sh
set -xv

directory=$1
iter=$2
id=$3


echo $1 $2 $3
./2_evs_from_dir.sh workspace/$directory $iter $id

