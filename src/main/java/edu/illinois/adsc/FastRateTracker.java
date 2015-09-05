package edu.illinois.adsc;

import sun.awt.Mutex;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;

/**
 * Created by Robert on 9/5/15.
 */
public class FastRateTracker{
    /* number of slides to keep in the history. */
    public final int _numOfSlides; // number of slides to keep in the history

    public final int _slideSizeInMils;
    private final long[] _histograms;// an array storing the number of element for each time slide.
    private boolean _simulate;// use simulated time rather than system time

    private long _ticks;// simulated ticks

    private Timer _timer;


    public FastRateTracker(int validTimeWindowInMils, int numOfSlides) {
        this(validTimeWindowInMils, numOfSlides, false);
    }

    public FastRateTracker(int validTimeWindowInMils, int numOfSlides, boolean simulate ){
        _numOfSlides = Math.max(numOfSlides, 1);
        _slideSizeInMils = validTimeWindowInMils / _numOfSlides;
        if (_slideSizeInMils < 1 ) {
            throw new IllegalArgumentException("Illeggal argument for RateTracker");
        }
        assert(_slideSizeInMils > 1);
        _histograms = new long[_numOfSlides];
        Arrays.fill(_histograms,0L);
        _timer = new Timer();
        _timer.scheduleAtFixedRate(new Fresher(),_slideSizeInMils,_slideSizeInMils);
    }

    public void notify(long count) {
        _histograms[_histograms.length-1]+=count;
    }

    /**
     * Return the average rate in slides.
     *
     * @return the average rate
     */
    public final float reportRate() {
        long sum = 0;
        long duration = _numOfSlides * _slideSizeInMils;
        for (long e : _histograms) {
            sum += e;
        }

        return sum / (float) duration * 1000;
    }

    private void updateSlides(){

        for (int i = 0; i < _numOfSlides - 1; i++) {
            _histograms[i] = _histograms[i + 1];
        }

        _histograms[_histograms.length - 1] = 0;
    }

    private class Fresher extends TimerTask {
        public void run () {
            updateSlides();
        }
    }

}
