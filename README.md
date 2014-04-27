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

Get a pre-built JAR from Bintray:

 [ ![Download](https://api.bintray.com/packages/lenz/maven/SlicedBread/images/download.png) ](https://bintray.com/lenz/maven/SlicedBread/_latestVersion)

When developing, we suggest downloading the the ''-sources'' and ''-javadoc'' JARs as well so you have everything in your IDE.


License
-------

Licensed under the LGPL.

