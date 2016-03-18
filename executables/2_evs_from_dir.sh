#!/bin/sh
directory=$1
distribution=$2
iter=$3
id=$4
preid=$5
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd $directory -evs P\(iterations, $iter\) A$distribution -id $preid_apostasy_$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd $directory -evs P\(iterations, $iter\) D$distribution -id $preid_destruction_$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd $directory -evs P\(iterations, $iter\) RF$distribution -id $preid_removal_$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd $directory -evs P\(iterations, $iter\) RP$distribution -id $preid_removal_partial_$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd $directory -evs P\(iterations, $iter\) CF$distribution -id $preid_conversion_$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd $directory -evs P\(iterations, $iter\) CP$distribution -id $preid_conversion_partial_$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd $directory -evs P\(iterations, $iter\) I$distribution -id $preid_invasion_$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd $directory -evs P\(iterations, $iter\) G$distribution -id $preid_genocide_$id
