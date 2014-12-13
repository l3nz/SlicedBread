
The MessagingConsole is a singleton object that is shared across all threads 
and acts as your access port to most of the features SlicedBread offers.

It lets you:

* Start multiple threads
* Create messaging ports to interact with those threads
* Send and receive messages 
* Run some utility functions

Getting a Console instance
==========================

This is really easy; in any class that requires SlicedBread you add something like:


        final static MessagingConsole console = MessagingConsole.getConsole();

The console will persist until the JVM closes.


Registering an existing thread
==============================

To obtain a messaging PID (and associated mailbox) on the console, you "register"
your current thread as:


        PID myself = console.registerExistingThread("Main");


This will create a new PID for the name "Main" and will open a mailbox.

To check if a mailbox exists or not, 


        PID areYouThere = console.findByDescription( "Printer/B" );


The PID will be null if no mailbox esists under that name, or you will get a valid PID 
if it does exist. 

Creating a new thread
=====================

In order to start new threads, you start them as:


        PID thread_A = console.register(myself, "Printer/B", new PrinterTask() );


This will:

* Start a new thread called "Printer/B" and run PrinterTask on it (PrinterTask must implement the 
  TaskProcess interface, see below). The thread name is set so it is easy to tell what is what using
  a debugger / profiler / whatever
* A new mailbox is created under the name "Printer/B" and the thread receives it as its main mailbox
* The "myself" thread is set as the parent of the new thread; life-cycle events pertaining to this 
  thread (e.g it started, it crashed, it terminated) are sent here.

If you want to stop the thread, you send it a MsgPleaseStop message, like:


        console.send( MsgPleaseStop.build(myself, thread_A) );


You will then receive a MsgProcessEneded when it stops. When a thread dies (because it stopped, or because 
it crashed, that is, threw an unhandled exception), the mailbox for the thread is removed, therefore any
thread can check whether another thread is running just by looking up its mailbox by name.  

Implementing a worker thread
============================

Any worker thread must implement the TaskProcess interface, that has one single method called run().


        @Override
        public void run() {

            PID ownPid = getOwnPid();
            log (
                "I am thread "  + getProcessDescription()
                + " (created by " + getParentDescription() + ")"
            );
            MessagingConsole console = getConsole();

            try {
                for ( ;; ) {
                    Msg m = console.receive(ownPid, 100);

                    if ( m instanceof MsgPleaseStop ) {
                        log( "Now Stopping" );
                        return;
                    } else
                    if ( m instanceof MsgPrint ) {
                        processMessage( (MsgPrint) m );
                    };
                }
            } catch ( InterruptedException e ) {
                return;
            }

        };


This is basically an endless loop that queries its mailbox for incoming messages, processes them
and  - if needed - sends results back. This object is given a mailbox and an instance of 
the console, plus a description and a PID of the parent.


Thread naming
=============

Every mailbox has a name. All worker threads have a mailbox that shares their startup name. They are free 
to have more, e.g. multiple threads "Printer/01" and "Printer/02" might share a mailbox "Printer" where 
jobs are actually queued. 

Listing threads
===============

You can easily get a list of active mailboxes by calling the .list() method. This returns the current
mailboxes, the number of messages queued on them and latency statistics.

Latency statistics looks like:

        Mailbox: T10#12 
        Current Size: 0
        Bin     0ms  1%     1106|              
        Bin     1ms  5%     4722|              
        Bin     2ms 16%    12859|##            
        Bin     4ms 34%    27904|####          
        Bin     8ms 41%    33122|#####   

Latency measurements are splitted into "bins" of logarithmic size. Latency is measured between the moment
a message is passed to the console to be queued and when it is pulled back from the console and returned.
Latency is measured in milliseconds.


Sending and receiving messages
==============================

Messages are meant to be immutable, built of immutable parts (ideally, all 'final' fields). 
Defensive copying is your friend here. We usually create them all with a builder pattern, 
so they all look something like:


        MsgPleaseStop.build(from, to)


All messges need to have a FROM and a TO mailbox that are not null. You create your own messages by 
extending the CustomMessage object.


In order to receive messages, you can either receive them one at a time or all at once.


        Msg m = console.receive( myPid, 200 );


Will yeld a valid message or 'null'; if no message is available, it will wait no more than 200ms for 
one to become available.  

As an alternative, you can ask for all queued messages; like in:


        List<Msg> lM = console.receiveAll( myPid ); 

Thread safety considerations
============================

The current version of the Console is backed by a ConcurrentHashMap, where

        Read operations can thus proceed without locking, but rely
        on selected uses of volatiles to ensure that completed
        write operations performed by other threads are noticed. 


So the cross-thread visibility is basically free at the application level. 

This does not prevent you to keep stray pointers and modifying (supposedly) immutable 
object at run time when held by other threads. You have all the rope you need to hang 
yourself with, so respect the [Golden Rules](Golden-rules.md) or you'll regret it.


''''

Back to the [SlicedBread Documentation Home Page](Home.md)


