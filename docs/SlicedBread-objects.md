
One of the design advantages of SlicedBread is that it is very compact, meaning that 
it uses very few object and so it is easy to learn.

== MessagingConsole

The MessagingConsole is a singleton object that is the core of the SlicedBread library.
See [[Working with the Console]] to know more.

== PID

The PID acts as a Process Identifier and as a mailbox identifier at the console level.
It is basically a string (plus an internal code) used for maintaining process- 
and mailbox-level identity.

== Message hierarchy

The base Message object is of type 'Msg'.

=== Common messages: MsgCommon

*MsgProcessStarted*

This is a notification to the parent that a process has started.

*MsgProcessEnded*

This is a notification to the parent that a process has terminated without errors.

*MsgPleaseStop*

This is a request to a thread to stop as soon as possible.

*MsgActionIds*

This message contains two numeric status codes and one string. Handy for quick prototipying.

=== Custom messages: MsgCustom

This is where you crete your messages from.

=== Error messages: MsgError

*MsgErrProcessDied*

The process has crashed.

*MsgErrRejectedMessage*

Not used anymore

TIP: you'd better not use.

*MsgErrUndeliverable*

The message is undeliverable (no destination mailbox found).



''''

Back to [[Home]]



