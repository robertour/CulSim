cultural-simulations
====================

This repository contains a set of java simulations for artificial societies. The simulations are related to the 
emergence of differenct cultures (groups of agents with the same features), and how to estabilize them in the
presence of noise, and destruction. It is a work in progress.

Right now, this code replicate the work of 
[Flache & Macy 2011](http://jcr.sagepub.com/content/early/2011/07/30/0022002711414371).

Flache & Marcy works presented a simulation that succesfully estabilize cultural groups that emerges bottom-up
from the interaction of agentes

The repository consists of an eclipse project, and initially it contains 4 simulations, based in the original
work and three experiments of Flache & Macy's paper.

1. **Axelrod (Seminal Work) - Axelrod.class**: A replication of the seminal work of 
[Axelrod 1997](http://jcr.sagepub.com/content/41/2/203.short).

2. **Homophily with interpersonal influence - FlacheExperiment1.class **: A replication of the seminal work of 
[Axelrod 1997](http://jcr.sagepub.com/content/41/2/203.short), with includes the modification of 
[Klemm et. al 2003](http://arxiv.org/abs/cond-mat/0205188), in which the estability of the system is easily 
perturbed by low levels of noise.

3. **Social influence model without homophily - FlacheExperiment2.class **: A replication of the solution introduced by 
[Parisi et. al 2003](http://jcr.sagepub.com/content/47/2/163.refs?patientinform-links=yes&legid=spjcr;47/2/163), 
in which social influence is introduced to estabilize the simulation.

4. ** Social influence model with homophily - FlacheExperiment1.class  **: A replication of the combination of the 
ideas of [Parisi et. al 2003](http://jcr.sagepub.com/content/47/2/163.refs?patientinform-links=yes&legid=spjcr;47/2/163)
and [Centola et. al 2007](http://jcr.sagepub.com/content/51/6/905.short)

Apart from that, it contains my simulations ( ** Ulloa[##].class **). They contain some ideas for my PhD Thesis: 
obtain stability (resilience to noise) of the culture with centralized repositories. Once I get interesting results, 
I will be updating this documentation, for now the headers of each `.java` file has a brief explanation of the main
ideas.

How to use it?
--------------

There is no live GUI. That means there is no screen where you can see the agents. If you want a better visual
understanding of the Axelrod model, I suggest you to start with any of the following implementations in Netlogo:

	http://ccl.northwestern.edu/netlogo/models/community/Axelrod_Cultural_Dissemination
	http://ccl.northwestern.edu/netlogo/models/community/cultura
	http://ccl.northwestern.edu/netlogo/models/community/Dissemination%20of%20Culture

Right now, the interface allows you to select the Experimental Design File (`sample.csv` is an example provided)
and the results folder. The Experimental Design File contains the following columns that you can play with:

- REPETIONS: # of times the configuration is repeated, for statistical analysis
- TYPE: The implementation. Possible values: [AXELROD|FLACHE_EXPERIMENT[1-3]|ULLOA[1-10|1B|1C]
- ITERATIONS: # of desired checkpoints
- CHECKPOINT: # of clicks (real iterations) before we collect some data. In each click COLS * ROWS randomly selected agents interact.  
- BUFFERED_SIZE: the size of the buffers. Bigger is better to avoid I/O outputs
- ROWS: # of rows of the world
- COLS:  # of columns of the world
- FEATURES: # of characteristics of the culture. Each feature represents a possible dimension of the culture, e.g. music
- TRAITS: # of possible values of the culture. Each trait represent a possible value for a feature, e.g. rock
- RADIUS: # the neighborhood of agents
- MUTATION: probability of a random change in the agent's vector after an interaction
- SELECTION_ERROR: probability of making a mistake in the selection of the agent with which the interaction will be performed

Then you have 4 buttons to control the simulation: `Start`, `Pause`, `Resume` and `Stop`. They should be self explanatory.

A folder `results` (or `results#` if `results` exist) is created, and it contains a file `results.csv` with 
the final results of each simulation. Also a folder called iterations that contains details of the iteration for
each simulation.

About this implementation
------------

This code was created with the goal of run fast. Sacrifices were made to achieve this:
1. There is no GUI that shows the evolution of the agents. All is kept in memory and I/O 
access is kept at the minimum.

2. Each simulation is run in a different core. There will be as many threads as your computer
have running at the same time. If you have 4 cores, you will have 4 threads (simulations) 
running simultaneously, and the rest waiting for them to finish.

3. The use of objects in the simulation is avoided. Instead, direct Java Matrices is used.
You will see a lot of confusing indexes, fun times!! In 10 years, you may have a powerful
machine at home to make a Netlogo version, and yet be able to run it in your phone. For now,
my laptop can not do any better. Yes, I tried Netlogo, it is just way to slow.
 
4. Modularization is avoided in order to reduce the number of method calls. This becomes
significant when a code has to run for 102 400 000 times (in a 32x32)

5. As a consequence of the previous, the code is repetitive. Each class (that inherits
from simulation) re-implement the wheel. You will find that most of the code in 
`run_experiment` is very repetitive, i.e. I didn't factorize intentionally.

6. Buffered writers are used, so I/O is reduced and the output to files is not immediate.


Installation
------------

Just run the cultural-simulator.jar with `java -jar cultural-simulator.jar`. 

