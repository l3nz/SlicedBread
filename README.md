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

