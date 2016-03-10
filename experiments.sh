#/bin/bash
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -ef $1
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -rd results -i 100000 -id no_catastrophy
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -rd results -i 100000 -evs DF\(U,1\) -id collapse_full_content
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -rd results -i 100000 -evs DS\(U,1\) -id collapse_full_structure
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -rd results -i 100000 -evs I\(U,0.25\) -id invasion
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -rd results -i 100000 -evs I\(N,0.5,0.5,0.1\) CP\(N,0.5,0.5,0.2\) -id invasion_with_partial_conversion
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -rd results -i 100000 -evs G\(U,0.8\) -id genocide80
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -rd results -i 100000 -evs G\(N,0.5,0.5,0.3\) I\(N,0.5,0.5,0.1\) DP\(U,0.1667\) CP\(U,0.1667\) -id genocide
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -rd results -i 100000 -evs G\(U,0.3\) I\(U,0.1\) DP\(U,0.1667\) CP\(U,0.1667\) -id genocide_U
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -ef $2 -id no_div_no_catastrophy
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -ef $2 -evs DF\(U,1\) -id no_div_collapse_full_content
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -ef $2 -evs DS\(U,1\) -id no_div_collapse_full_structure
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -ef $2 -evs I\(U,0.25\) -id no_div_invasion
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -ef $2 -evs I\(N,0.5,0.5,0.1\) CP\(N,0.5,0.5,0.2\) -id no_div_invasion_with_partial_conversion
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -ef $2 -evs G\(U,0.8\) -id no_div_genocide80
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -ef $2 -evs G\(N,0.5,0.5,0.3\) I\(N,0.5,0.5,0.1\) DP\(U,0.1667\) CP\(U,0.1667\) -id no_div_genocide
java -Xss20m -Xms2g -Xmx2g -jar cultural-simulator.jar  -ef $2 -evs G\(U,0.3\) I\(U,0.1\) DP\(U,0.1667\) CP\(U,0.1667\) -id no_div_genocide_U
