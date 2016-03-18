#!/bin/sh
csvfile=$1
distribution=$2
id=$3
preid=$4
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $csvfile -evs A$distribution -id $preidapostasy$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $csvfile -evs D$distribution -id $preiddestruction$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $csvfile -evs RF$distribution -id $preidremoval$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $csvfile -evs RP$distribution -id $preidremoval_partial$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $csvfile -evs CF$distribution -id $preidconversion$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $csvfile -evs CP$distribution -id $preidconversion_partial$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $csvfile -evs I$distribution -id invasion$id
java -Xss100m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $csvfile -evs G$distribution -id $preidgenocide$id
