package edu.illinois.adsc;

import java.util.Arrays;

/**
 * This class is a plugin to track the rate.
 */
public class RateTracker {
    /* number of slides to keep in the history. */
    public final int _numOfSlides; // number of slides to keep in the history

    public final int _slideSizeInMils;
    private final long[] _histograms;// an array storing the number of element for each time slide.
    private long _latestSlideId;// slide id upon last update to the histograms
    private boolean _simulate;// use simulated time rather than system time

    private long _ticks;// simulated ticks

    /**
     * @param validTimeWindowInMils events that happened before validTimeWindowInMils are not considered
     *                        when reporting the rate.
     * @param numOfSlides the number of time sildes to divide validTimeWindows. The more slides,
     *                    the smother the reported results will be.
     */
    public RateTracker(int validTimeWindowInMils, int numOfSlides) {
        this(validTimeWindowInMils, numOfSlides, false);
    }

    /**
     * Constructor
     * @param validTimeWindowInMils events that happened before validTimeWindow are not considered
     *                        when reporting the rate.
     * @param numOfSlides the number of time sildes to divide validTimeWindows. The more slides,
     *                    the smother the reported results will be.
     * @param simulate set true if it use simulated time rather than system time for testing purpose.
     */
    public RateTracker(int validTimeWindowInMils, int numOfSlides, boolean simulate) {
        _numOfSlides = Math.max(numOfSlides, 1);
        _slideSizeInMils = validTimeWindowInMils / _numOfSlides;
        assert(_slideSizeInMils > 1);
        _simulate = simulate;
        _ticks = 0;
        _latestSlideId = getCurrentWindowId();
        _histograms = new long[_numOfSlides];
        Arrays.fill(_histograms, 0);
    }
    /**
     * Notify the tracker upon new arrivals
     *
     * @param count number of arrivals
     */
    public final void notify(long count) {
        updateHistograms();
        _histograms[_numOfSlides - 1] += count;
    }

    /**
     * Return the average rate in slides.
     *
     * @return the average rate
     */
    public final float reportRate() {
        long sum = 0;
        long duration = _numOfSlides * _slideSizeInMils;
        updateHistograms();
        for (long e : _histograms) {
            sum += e;
        }

        return sum / (float) duration * 1000;
    }

    public int get_slideSizeInMils() {
        return _slideSizeInMils;
    }

    public int get_numOfSlides() {
        return _numOfSlides;
    }

    /**
     * Get the simulated ticks.
     *
     * @return the simulated ticks.
     */
    public final long get_ticks() {
        return _ticks;
    }

    /**
     * Set the ticks. (for testing)
     *
     * @param ticks: the simulated ticks
     */
    public final void set_ticks(long ticks) {
        this._ticks = ticks;
    }

    private long getCurrentWindowId() {
        return (getSlideId(getCurrentSystemTime()));
    }

    private long getCurrentSystemTime() {
        if (_simulate)
            return _ticks;
        else
            return System.currentTimeMillis();
    }

    /* update the histograms to the current slide id*/
    private void updateHistograms() {
        long windowsDiff, retainedWindows;

        windowsDiff = getCurrentWindowId() - _latestSlideId;

        if (windowsDiff == 0)
            return;

        retainedWindows = Math.max(0, _numOfSlides - windowsDiff);

        for (int i = 0; i < _numOfSlides; i++) {
            if (i < retainedWindows)
                _histograms[i] = _histograms[i + (int) windowsDiff];
            else
                _histograms[i] = 0;
        }

        _latestSlideId = getCurrentWindowId();

    }

    private long getSlideId(long ticks) {
        return ticks / _slideSizeInMils;
    }
}