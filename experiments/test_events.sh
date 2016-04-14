#!/bin/sh

param=0.16667
posid=1_6
iter=1000
echo $param $posid $iter
./3_batch_from_dir.sh div $param $posid $iter
./3_batch_from_dir.sh no_div $param $posid $iter
