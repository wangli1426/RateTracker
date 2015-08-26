## RateTracker
A utility developped by Li Wang, to keep tracks of the arrival rate upon certain event.


###Usage:

1. Upon new event arrivals, just call the ``notify()`` to notify RateTracker. 

2. Call ``reportRate()`` to retrieve the recent arrival rate. 

###Features of RateTracker:
1. *Very Small Memory Consumption:* RateTracker automatically cleans the counts of records that not happened recently. 
2. *Efficiency:* There is no expilict thread to clean the expired counts; cleanance is triggered by ``notify()`` and ``reportRate()`` in an efficient way.

If you have any question or suggestion, feel free to contract me.

