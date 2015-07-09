
Thread Pools
============

Thread pools let you create pools of threads that share the same mailbox.
This way, when you send a message to the mailbox, the first available thread will 
take it and process it.

Thread pools, when running, create the fllowing mailboxes

* MYPOOL: This is the main mailbox that threads will pull mesages from
* SbPools/MYPOOL/1, /2 and so on: a mailbox for each thread taking part in the pool
* SbPools/MYPOOL/CTRL: a control mailbox

From the point of view of the user, you should only be concerned with the main
mailbox (and the Watchdog). When creating a thread pool, the main mailbox is created when the pool
is ready and destroyed when the pool terminates.

Thread pools receive special messages of class "MsgPoolRunnable", that are to be
sent to the main mailbox and contain an object implementing RTask. 

RTask  is a simple interface that defines an action to be run on the pool; it has just one 
method called run() that will be started and will run to completion. It expects you to 
return a message (for example to notify the caller that an action was performed) or null
in case no response is needed.

Tasks on the pool are supposed to be (relatively) short-lived and to terminate
as soon as they are done for. 


Creating and destroying thread pools
====================================

Thread pools have a name, that matches the name of the main mailbox.
As thread pools require some handling, in order to use them you need to 
first start a thread called Watchdog that wil take care of creating and 
managing them.

You create the Watchdog thread by running:

    PID watchdog = WatchdogTask.up();
        
This will start a Watchdog thread if not present. As the operation is inexpensive
if the Watchdog is present, you can use it as a way to get a reference to the 
Watchdog's mailbox that will take care of starting it if needed. The Watchdog is a 
permanent thread that will manage pools and runs scheduled actions.

In order to start a pool, you will then ask the Watchdog to create it for you by 
setting a maximum and minimum number of threads in the pool;
in order to know when it is ready, wou can wait for the mailbox to become available.

    mc.send( MsgThreadPool.build(me, watchdog, "MYPOOL", 5, 5));
                    
    assertTrue( "Pool should be up", SbTools.awaitMailboxUp("MYPOOL", 10000));

At themoment the number of threads in the pool is fixed, but we plan to be able to add 
and remove threads as needed, in a way similar to Java's ForkJoinPool (threads idle for 
too long will terminate, and will be created as needed).

In order to destroy a thread pool, you will send a message like:

    mc.send(MsgThreadPool.buildShutdown(me, watchdog, "MYPOOL"));
    
    assertTrue( "Pool should be down", SbTools.awaitMailboxDown("MYPOOL", 10000));

And to destroy the Watchdog:

    WatchdogTask.down();



Using thread pools
==================

In order to run an action on a thread pool, you send it to the main mailbox; like in the example:


    console.send(MsgPoolRunnable.build(myPid, poolPid, new RTask() {

        public Msg process() {
            Logger ll = .....
            ll.error( " - Starting task");
            SbTools.sleep(1000);
            ll.error( " - Ending task");
            return null;
        }
        
    }));

As you can see, in this case the function to be run just prints a line of log, waits for one second and prints
a second line. As we do not need to return any message, we return null. If we wanted, we could return a message 
to be sent to some other thread.

Running deferred and recurring tasks
====================================

By leveraging the Watchdog, we can run deferred tasks and recurring tasks. A deferred task is created by
sending the task to the Watchdog, that will keep it on a list until the time specified matches the requested time;
at this point, the Watchdog will queue it on the pool's mailbox.

In the case below, we create a deferred message that will be run 

    console.send( MsgPoolDeferred.build(me, watchdog, poolPid, 1500, 
        new RTask() {

            public Msg process() {
                Logger ll = .....
                ll.error( " - Starting task");
                SbTools.sleep(1000);
                ll.error( " - Ending task");
                return null;
            }
        }
    ));

In order to have recurring messages, what we do is creating RTasks that return a MsgPoolDeferred object to 
be delivered to the Watchdog. This way the Watchdog will keep it until the required timeout expires. Please note
that it is perfectly valid to return the original RTask; this way it will be run as long as needed, and the 
same object will be run multiple times (so be careful of potential memory leaks!).

So to have our object reschedule 5 times and no more, after 2000 ms each, you could define an RTask like....

    int i = 0;

    public Msg process() {
        String preamble = "Task " + taskName + " (iteration " + i + ") ";
        logger.error(preamble + "starting at " + System.currentTimeMillis());

        SbTools.sleep(1000);
        i = i + 1;
        
        logger.error(preamble + "stopping at " + System.currentTimeMillis());

        if (i < maxCycles) {
            return MsgPoolDeferred.build(fromPid, watchDogPid, poolPid, 2000, this);
        } else {
            return null;
        }
    }


That's all we have for now.



''''

Back to the [SlicedBread Documentation Home Page](Home.md)


