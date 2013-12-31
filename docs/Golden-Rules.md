
The golden rules are as follows:

* Create your own messages by extending CustomMsg
* Create immutable builders for your messages - you do not need to make them
  actually immutable by using "final" everywhere, as cross-thread visibility is
  guaranteed by the underlying message broker. This said, it is probably
  a good idea to do it anyway.
* No sharing of mutables - whatever is embedded in an object must either 
  be immutable (and if it is, you can keep references to it) or you must 
  make sure that you keep no references to it. Best practice would be to 
  use immutable objects, but defensive copying works as well.
* In order to create a new thread, extend TaskProcess and implement its run() 
  method to be an infinite loop obeying the PleaseStop message.

This is basically it. Happy hacking!



Back to the [SlicedBread Documentation Home Page](Home.md)

