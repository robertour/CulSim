#!/bin/sh
directory=$1
value=$2
id=$3
preid=$1
iter=$4
echo $1 $2 $3 $4
./2_evs_from_dir.sh workspace/$directory \(U\,$value\) $iter "U_"$id $preid
./2_evs_from_dir.sh workspace/$directory \(N\,0.5\,0.5\,$value\) $iter "N_"$id $preid
