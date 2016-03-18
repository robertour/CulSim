#!/bin/sh
directory=$1
value=$2
id=$3
preid=$1
iter=1000
./2_evs_from_dir workspace/$directory \(U\,$value\) 1000 U_$id $preid
./2_evs_from_dir workspace/$directory \(N\,0.5\,0.5\,$value\) 1000 N_$id $preid