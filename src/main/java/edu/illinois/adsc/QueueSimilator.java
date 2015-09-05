package edu.illinois.adsc;

import javax.rmi.CORBA.Util;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Robert on 9/3/15.
 */
public class QueueSimilator {
    final private Queue _queue;
    final private FastRateTracker _rateTracker;

    public QueueSimilator() {
        _queue = new ConcurrentLinkedQueue<Integer>();
        _rateTracker = new FastRateTracker(10000, 10);
    }

    public void produce(long e) {
        _queue.add(e);
        _rateTracker.notify(1);
    }

    public long consume() {
        return (Long)_queue.poll();
    }

    Thread createConsumer(float rate) {
        Runnable ret = new Consumer(rate);

        return new Thread(ret);
    }

    Thread createProducer(float rate) {
        Runnable ret = new Producer(rate);

        return new Thread(ret);
    }

    Thread createMonitor(int interval) {
        Runnable ret = new Monitor(interval);

        return new Thread(ret);
    }


    class Consumer implements Runnable{

        final private long interval;

        public Consumer(float rate) {

            interval = (long)(1 / rate * 1000);

        }
        public void run() {
            try{
                while(true) {
                    Thread.sleep(interval);
                    consume();
                }
            }
            catch (Exception e){

            }

        }
    }

    class Producer extends Thread{

        final private long interval;
        private int element;

        public Producer(float rate) {

            interval = (long)(1 / rate * 1000);

            element=0;

        }
        public void run() {
            try{
                while(true) {
                    Thread.sleep(interval);
                    produce(System.currentTimeMillis());
                }
            }
            catch (Exception e){

            }

        }
    }

    class Monitor extends Thread {

        final private int _reportCyclesInSecs;

        public Monitor(int reportCyclesInSecs){
            _reportCyclesInSecs = reportCyclesInSecs;
        }

        public void run(){
            while(true) {
                try {
                    Thread.sleep(1000 * _reportCyclesInSecs);
                }
                catch (Exception e){

                }
                System.out.println("Queue length:" + _queue.size() + "\tArrival rate:" + _rateTracker.reportRate()
                        + "\tSojourn:" + (System.currentTimeMillis()-(Long) _queue.peek()) / 1000.0 +
                                    "\tEstimate Sojourn:" +_queue.size() / _rateTracker.reportRate());
            }
        }
    }
    static public void main(String[] args){

        FastRateTracker rt = new FastRateTracker(1000,10);

        long start = System.currentTimeMillis();
        for(int i=0; i<100000000; i++) {
            rt.notify(System.currentTimeMillis()%2);
        }

        System.out.println(100000000 / (System.currentTimeMillis() - start) *1000);




        try{
            Thread.sleep(1000);
            QueueSimilator queueSimulator = new QueueSimilator();
            Thread producer = queueSimulator.createProducer(1000);
            Thread consumer = queueSimulator.createConsumer(1000);
            Thread monitor = queueSimulator.createMonitor(1);

            monitor.start();

            producer.start();

            Thread.sleep(2000);

            consumer.start();

            Thread.sleep(10000);
        }
        catch (Exception e){

        }


    }
}
