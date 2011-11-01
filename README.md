SlicedBread - Java multithreading made easy
===========================================

SlicedBread is a library that makes it simpler to program multithreaded
applications in Java. It does so by doing away with "classic" multithreaded 
staples - that is, shared state using thread-safe objects, locks and synchronizers 
- versus a set of single-thread processes that
send and receive immutable messages. In a sense it is inspired by Erlang's 
messaging system. In another, the over 400 rich pages of "Java concurrency 
in practice" show how hard it is to write and debug a good-mannered multithreaded
application in Java.

SlicedBread has been used in production systems for quite a bit of time now with no major 
issues, so I thought it was something that could benefit the community at large
and I'm sharing it.

SlicedBread is licensed under the LGPL.

Sample application
------------------

The sample application is available at under the sources as:

----
		ch.loway.oss.sbDemos.helloWorld.HelloWorld
----

It basically spawns a couple of threads and sends each a simple message
so that one prints "Hello" and the other "World".

The output looks something like:

----
		Hello world starting
		(Thread A#2) I am thread Thread A (created by Main)
		(Thread A#2) PRINTING: Hello
		(Thread A#2) Now Stopping
		(Thread B#3) I am thread Thread B (created by Main)
		(Thread B#3) PRINTING: World
		(Thread B#3) Now Stopping
		Message found: F:Thread A#2 T:Main#1 - ProcessStarted
		Message found: F:Thread A#2 T:Main#1 - ProcessEnded 
		Message found: F:Thread B#3 T:Main#1 - ProcessStarted
		Message found: F:Thread B#3 T:Main#1 - ProcessEnded 
----

So if you are curious before some actual docs are released, that is a good place to start.

:-)

You can also run it straight from the JAR artifact, by running it as:

----
    $ java -cp "lib/log4j-1.2.16.jar;SlicedBread-0.0.1.jar" ch.loway.oss.sbDemos.helloWorld.HelloWorld
----

The golden rules
================

* Create your own messages by extending CustomMsg
* Create immutable builders for your messages, and make them "final", so that 
  they are always visible on muti-core systems
* No sharing of mutables - whatever is embedded in an object must either be immutable
  (and if it is, you can keep references to it) or you must make sure that you keep 
  no references to it. Best practice would be to use immutable objects, but defensive
  copying works as well.
* In order to create a new thread, extend  TaskProcess and implement its run() method to be
   an infinite loop obeying the PleaseStop message.

This is basically it. Happy hacking!
