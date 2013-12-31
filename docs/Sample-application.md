
The sample application is available under the source tree as:


		ch.loway.oss.sbDemos.helloWorld.HelloWorld


It basically spawns a couple of threads and sends each a simple message so that one prints "Hello" and the other "World".

The output looks something like:

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

So if you are curious before some actual docs are released, that is a good place to start.

:-)

You can also run it straight from the JAR artifact, by running it as:

		$ java -cp "lib/log4j-1.2.16.jar;SlicedBread-0.0.1.jar" ch.loway.oss.sbDemos.helloWorld.HelloWorld





Back to the [SlicedBread Documentation Home Page](Home.md)
