cultural-simulations
====================

This repository contains a set of java simulations for artificial societies. The simulations are related to the 
emergence of differenct cultures (groups of agents with the same features), and how to estabilize them in the
presence of noise, and destruction. It is a work in progress.

Right now, this code replicate the work of 
[Flache & Macy 2011](http://jcr.sagepub.com/content/early/2011/07/30/0022002711414371).

Flache & Marcy works presented a simulation that succesfully estabilize cultural groups that emerges bottom-up
from the interaction of agentes

The repository consists of an eclipse project, and right now it contains three simulations, based in the three 
experiments of the paper.

1. **Homophily with interpersonal influence**: A replication of the seminal work of 
[Axelrod 1997](http://jcr.sagepub.com/content/41/2/203.short), with includes the modification of 
[Klemm et. al 2003](http://arxiv.org/abs/cond-mat/0205188), in which the estability of the system is easily 
perturbed by low levels of noise.

2. **Social influence model without homophily**: A replication of the solution introduced by 
[Parisi et. al 2003](http://jcr.sagepub.com/content/47/2/163.refs?patientinform-links=yes&legid=spjcr;47/2/163), 
in which social influence is introduced to estabilize the simulation.

3. ** Social influence model with homophily **: A replication of the combination of the works of the ideas of
[Parisi et. al 2003](http://jcr.sagepub.com/content/47/2/163.refs?patientinform-links=yes&legid=spjcr;47/2/163)
and [Centola et. al 2007](http://jcr.sagepub.com/content/51/6/905.short)

How to use it?
--------------

There is no GUI :(. Well, right now there is a button to run the experiments on the file called `sample.csv`.
That I hope it is self speaking after reading the papers.


Installation
------------

You can just download the repository and compile the java files, or install eclipse and pull the repository from there.
Sorry, right now Java knowledge is mandatory. But if you are interested, don't hesitate in write me.


