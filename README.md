cultural-simulations
=================

Summary
-------

This repository contains a set of java simulations for artificial societies. The simulations are related to the 
emergence of different cultures (groups of agents with the same features), and how to estabilize them in the
presence of noise, and destruction. 

The code implements several extensions to the seminal work of [Axelrod 1997](http://jcr.sagepub.com/content/41/2/203.short). It includes mutation, a source of perturbation to the cultural traits as proposed by [Klemm et. al 2003](http://arxiv.org/abs/cond-mat/0205188), multilateral social influence, initial suggested by [Parisi et. al  2003](http://jcr.sagepub.com/content/47/2/163.refs?patientinform-links=yes&legid=spjcr;47/2/163) and [Centola et. al 2007](http://jcr.sagepub.com/content/51/6/905.short), and finally adapted by [Flache & Macy 2011](http://jcr.sagepub.com/content/early/2011/07/30/0022002711414371) who also introduce another source of noise: selection error. Finally, it also includes institutions as we proposed in [Ulloa et. al.](http://journals.plos.org/plosone/article?id=10.1371%2Fjournal.pone.0153334), including institutional process such as propaganda and democracy.

Apart from that, now it includes catastrophic (or no so catastrophic) events that affects the simulation in execution times. These events simulates catastrophic events such as genocide, invasion, attacks to institutions content and structure, and loss of faith in institutions (apostasy), but also not necessarily catastrophic events such as change of noise, democracy or institutional influence rates.


How to use it?
--------------

The wiki (user manual) is [now available](https://github.com/robertour/cultural-simulations/wiki), and a GUI; the [Quick Start] (https://github.com/robertour/cultural-simulations/wiki/A.-Quick-Start) will introduce you very fast.

There is also a [Batch Mode](https://github.com/robertour/cultural-simulations/wiki/I.-Batch-Mode) and a [Command Line Interface](https://github.com/robertour/cultural-simulations/wiki/J.-Command-Line-Interface) to run experimental designs. Statistical analysis of the resutls is possible through the [Output Files](https://github.com/robertour/cultural-simulations/wiki/H.-Output-Files).


About this implementation
--------------------------

This code was created with the goal of running fast, most of them apply to the Batch Mode or Command Line Interfacce:

1. All is kept in memory and I/O access is kept at the minimum.

2. Each simulation is run in a different core. There will be as many threads as your computer have running at the same time. If you have 4 cores, you will have 4 threads (simulations) running simultaneously, and the rest waiting for them to finish.

3. The use of objects in the simulation is avoided in the classes that correspond to the core implementation of the simulation. Instead, direct Java Matrices is used. You will see a lot of confusing indexes, fun times!! I did my best to keep decent names for them without being extremely verbose, and it is very well documented.
 
4. Modularization in the simulation core method is avoided in order to reduce the number of method calls. 

5. As a consequence of the previous, the code is repetitive. Each class (that inherits from simulation) re-implement the wheel. You will find that most of the code in `run_experiment` is very repetitive, i.e. I didn't factorize intentionally.

6. Buffered writers are used, so I/O is reduced and the output to files is not immediate.


Installation
-------------

Install Java 1.7 or 1.8. It also need to be tested with IcedTea because it was not working a few months ago; if it doesnt, Oracle will have to be.

###### For **windows users**: 

 * If you don't know how to use the terminal/command line, try:
  1. Download the `culsim.jar` file from [here](https://github.com/robertour/CulSim/commit/1c45dc3c8d5cfb2051afe1d8aa70264facdad046)
  2. Right click in the `culsim.jar` file
  3. Open With...
  4. Select "Java runtime environment" (if the option doesn't appear, please [install Java 7 first](https://www.java.com/en/download/help/windows_manual_download.xml))
  5. After that double click should be enough.
 
 * Alternatively, you can double click the `culsim.bat`
 
 * If not the open the Windows CMD:
  1. Go to the directory that contains the `culsim.jar`
  2. Write  `java -jar culsim.jar`
  3. Hit Enter

###### For **Linux/Unix users**: 

  1. Press `Ctrol+t`
  2. Go to the directory that contains the `culsim.jar` and `culsim.sh`
  2. Write `./culsim.sh` or `java -jar culsim.jar`
  3. Hit Enter

###### For **Mac users**: 

  It should be the same as for Linux/Unix users, but I have no relation with macs, so please let me know.
