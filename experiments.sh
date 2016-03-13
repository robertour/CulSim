#!/bin/bash
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $1
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -id no_catastrophy
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs A\(U,0.2\) -id apostasy_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs A\(U,0.8\) -id apostasy_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs A\(N,0.5,0.5,0.2\) -id apostasy_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs A\(N,0.5,0.5,0.8\) -id apostasy_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs D\(U,0.1667\) -id destruction_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs D\(U,1.0\) -id destruction_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs D\(N,0.5,0.5,0.2\) -id destruction_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs D\(N,0.5,0.5,0.8\) -id destruction_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs RF\(U,0.1667\) -id removal_0_16
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs RF\(U,0.833\) -id removal_0_83
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs RF\(N,0.5,0.5,0.2\) -id removal_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs RF\(N,0.5,0.5,0.8\) -id removal_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs RP\(U,0.1667\) -id removal_partial_0_16
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs RP\(U,0.833\) -id removal_partial_0_83
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs RP\(N,0.5,0.5,0.2\) -id removal_partial_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs RP\(N,0.5,0.5,0.8\) -id removal_partial_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs CF\(U,0.1667\) -id conversion_0_16
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs CF\(U,0.833\) -id conversion_0_83
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs CF\(N,0.5,0.5,0.2\) -id conversion_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs CF\(N,0.5,0.5,0.8\) -id conversion_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs CP\(U,0.1667\) -id conversion_partial_0_16
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs CP\(U,0.833\) -id conversion_partial_0_83
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs CP\(N,0.5,0.5,0.2\) -id conversion_partial_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs CP\(N,0.5,0.5,0.8\) -id conversion_partial_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs I\(U,0.2\) -id invasion_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs I\(U,0.8\) -id invasion_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs I\(N,0.5,0.5,0.2\) -id invasion_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs I\(N,0.5,0.5,0.8\) -id invasion_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs G\(U,0.2\) -id genocide_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs G\(U,0.8\) -id genocide_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs G\(N,0.5,0.5,0.2\) -id genocide_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -rd results -i 100000 -evs G\(N,0.5,0.5,0.8\) -id genocide_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -id no_div_no_catastrophy
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -i 100000 -id no_div_no_catastrophy
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs A\(U,0.2\) -id no_div_apostasy_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs A\(U,0.8\) -id no_div_apostasy_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs A\(N,0.5,0.5,0.2\) -id no_div_apostasy_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs A\(N,0.5,0.5,0.8\) -id no_div_apostasy_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs D\(U,0.1667\) -id no_div_destruction_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs D\(U,1.0\) -id no_div_destruction_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs D\(N,0.5,0.5,0.2\) -id no_div_destruction_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs D\(N,0.5,0.5,0.8\) -id no_div_destruction_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs RF\(U,0.1667\) -id no_div_removal_0_16
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs RF\(U,0.833\) -id no_div_removal_0_83
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs RF\(N,0.5,0.5,0.2\) -id no_div_removal_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs RF\(N,0.5,0.5,0.8\) -id no_div_removal_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs RP\(U,0.1667\) -id no_div_removal_partial_0_16
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs RP\(U,0.833\) -id no_div_removal_partial_0_83
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs RP\(N,0.5,0.5,0.2\) -id no_div_removal_partial_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs RP\(N,0.5,0.5,0.8\) -id no_div_removal_partial_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs CF\(U,0.1667\) -id no_div_conversion_0_16
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs CF\(U,0.833\) -id no_div_conversion_0_83
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs CF\(N,0.5,0.5,0.2\) -id no_div_conversion_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs CF\(N,0.5,0.5,0.8\) -id no_div_conversion_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs CP\(U,0.1667\) -id no_div_conversion_partial_0_16
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs CP\(U,0.833\) -id no_div_conversion_partial_0_83
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs CP\(N,0.5,0.5,0.2\) -id no_div_conversion_partial_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs CP\(N,0.5,0.5,0.8\) -id no_div_conversion_partial_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs I\(U,0.2\) -invasion_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs I\(U,0.8\) -id no_div_invasion_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs I\(N,0.5,0.5,0.2\) -id no_div_invasion_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs I\(N,0.5,0.5,0.8\) -id no_div_invasion_N_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs G\(U,0.2\) -id no_div_genocide_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs G\(U,0.8\) -id no_div_genocide_0_8
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs G\(N,0.5,0.5,0.2\) -id no_div_genocide_N_0_2
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar -ef $2 -evs G\(N,0.5,0.5,0.8\) -id no_div_genocide_N_0_8
