#!/bin/sh
directory=$1
value=$2
id_param=$3
preid=$1
iter=$4
echo $1 $2 $3 $4
./2_evs_from_dir.sh workspace/$directory @U\,$value $iter $preid "U_"$id_param
./2_evs_from_dir.sh workspace/$directory @N\,0.5\,0.5\,$value $iter $preid "N_"$id_param 
