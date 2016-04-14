#!/bin/sh
param=0.6667
posid=4_6
iter=100000
echo $param $posid $iter
./3_batch_from_dir.sh div $param $posid $iter
./3_batch_from_dir.sh no_div $param $posid $iter
