SlicedBread - Java multithreading made easy
===========================================

SlicedBread is a library that makes it simpler to program multithreaded
applications in Java. 

It does so by doing away with "classic" multithreaded 
staples - that is, shared state using thread-safe objects, locks and 
synchronizers - versus a set of single-thread processes that
send and receive immutable messages. 

In a sense it is inspired by Erlang's 
messaging system, that is in itself based on the Actors model. 
In another, the over 400 rich pages of "Java concurrency 
in practice" show how hard it is to write and debug a good-mannered multithreaded
application in standard Java.

SlicedBread has been used in production systems for quite a bit of time now with no major 
issues, so I thought it was something that could benefit the community at large
and I'm sharing it.

SlicedBread is licensed under the LGPL.

The documentation for the project is within the ''docs'' folder.

Happy hacking!

View the [SlicedBread Documentation Home Page](docs/Home.md)





What's new
----------

* Version 0.4.0: improved thread pools (with [docs](docs/ThreadPools.md) as well) and RTasks.
* Version 0.3.1: added thread pools and better printout of latency metrics.
* Version 0.2.0: added latency tracking framework.


Building
--------

Requires Gradle.

Get a copy of the GitHub repo and type:

	 git clone https://github.com/l3nz/SlicedBread.git	
     cd SlicedBread
     gradle clean build

To have everything built from scratch. Dependencies are handled automatically by Gradle.


### Dependencies ###

At the moment, we only require ''slf4j''. 


Downloads
---------

This library used to be on JCenter. Frankly, I cannot be bothered to jump through all the hoops to publish on Maven Central - I do this in my spare time, give me a break. So you can build it on your own and publish on your private repo - I enclose a recipe for PomFrites https://github.com/l3nz/ObjectiveSync.git to make it easier. Enjoy.



License
-------

Licensed under the LGPL (v3).

