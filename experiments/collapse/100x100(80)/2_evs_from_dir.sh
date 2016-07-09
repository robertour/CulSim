#!/bin/sh
set -xv

directory=$1
distribution=$2
iter=$3
preid=$4
id=$5
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -rd $directory -evs P@iterations,$iter A$distribution -id $preid"_apostasy_"$id
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -rd $directory -evs P@iterations,$iter D$distribution -id $preid"_destruction_"$id
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -rd $directory -evs P@iterations,$iter RF$distribution -id $preid"_removal_full_"$id
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -rd $directory -evs P@iterations,$iter RP$distribution -id $preid"_removal_partial_"$id
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -rd $directory -evs P@iterations,$iter CF$distribution -id $preid"_conversion_full_"$id
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -rd $directory -evs P@iterations,$iter CP$distribution -id $preid"_conversion_partial_"$id
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -rd $directory -evs P@iterations,$iter I$distribution -id $preid"_invasion_"$id
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -rd $directory -evs P@iterations,$iter G$distribution -id $preid"_genocide_"$id
