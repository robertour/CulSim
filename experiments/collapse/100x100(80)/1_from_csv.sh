#!/bin/sh
csv_file=$1
id=$2
iter=$3
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -ef $csv_file -id $id
java -Xss100m -Xms2g -Xmx2g -jar ../../../executables/culsim.jar -rd workspace/$id -evs P@iterations,$iter -id $id"_no_catastrophy"
