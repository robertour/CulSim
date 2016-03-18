#!/bin/sh
csvfile=$1
distribution=$2
id=$3
preid=$4
3_evs_from_csv $csvfile \(U,$distribution\) _U_$id $preid
3_evs_from_csv $csvfile \(N,0.5,0.5,$distribution\) _N_$id $preid