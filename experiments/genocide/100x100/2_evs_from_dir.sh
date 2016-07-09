#!/bin/sh
set -xv

directory=$1
iter=$2
id=$3
apostasy="U,0.5"
normal="E,0.5,0.5,0.75,0.25"
partial_content="U,0.2"
genocide="E,0.5,0.5,0.7,0.1"
invasion="U,0.03"
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -rd $directory -evs P@iterations,$iter "A@"$apostasy D@$normal RP@$partial_content RP@$normal RF@$normal G@$genocide I@$invasion CF@$normal CP@$normal -id $id

