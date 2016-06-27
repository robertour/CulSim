#!/bin/sh
java -Xss100m -Xms2g -Xmx2g -jar ../../executables/culsim.jar -ef test.csv -id test
java -Xss100m -Xms2g -Xmx2g -jar ../../executables/culsim.jar -rd workspace/test -evs_file comb.event -id test_event
