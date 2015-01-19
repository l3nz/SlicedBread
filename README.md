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
I created a small, no-pretence space for discussion and ideas on Google Plus - 
find it at [SlicedBread community](https://plus.google.com/u/0/communities/115965331294802598233) .

Happy hacking!

View the [SlicedBread Documentation Home Page](docs/Home.md)


What's new
----------

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

In most cases you do not need to do a manual download, just include JCenter as a central Maven repo:


	repositories {
		mavenCentral()
	    mavenRepo(url: 'http://jcenter.bintray.com') 
	}


	dependencies {
	    compile 'ch.loway.oss.slicedbread:slicedbread:0.3.1'
	}

As an alternative, you can get a pre-built JAR from Bintray:

 [ ![Download](https://api.bintray.com/packages/lenz/maven/SlicedBread/images/download.png) ](https://bintray.com/lenz/maven/SlicedBread/_latestVersion)

When developing, we suggest downloading the the ''-sources'' and ''-javadoc'' JARs as well so you have everything in your IDE.


License
-------

Licensed under the LGPL (v3).

