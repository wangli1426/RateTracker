package edu.illinois.adsc;

import java.util.Arrays;

/**
 * This class is a plugin to track the rate.
 */
public class RateTracker {
    /* number of slides to keep in the history. the more slides, the smother the reported rate is. */
    public final static int numOfSlides = 10;

    public final static int slideSizeInMils = 100;
    private final long[] _histogram = new long[numOfSlides];// an array storing the number of element for each time slide.
    private long _latestSlideId;//SlideID upon last update to the histogram
    private boolean _simulate;// use simulate ticks rather than system ticks

    private long _ticks;// simulated ticks

    public RateTracker() {
        this(false);
    }

    public RateTracker(boolean simulate) {
        _simulate = simulate;
        _ticks = 0;
        _latestSlideId = getCurrentWindowId();
        Arrays.fill(_histogram, 0);
    }

    public static void main(String[] args) {
        RateTracker rt = new RateTracker(true);
        rt.notify(1);
        float r1 = rt.reportRate();
        rt._ticks += RateTracker.slideSizeInMils;
        rt.notify(1);
        float r2 = rt.reportRate();
        System.out.print("r1:" + r1 + "r2:" + r2);
        System.out.print((r2 == r1 * 2));
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

    /**
     * Notify the tracker upon new arrivals
     *
     * @param count number of arrivals
     */
    public final void notify(int count) {
        updateHistorgrams();
        _histogram[numOfSlides - 1] += count;
    }

    /**
     * Return the average rate in slides.
     *
     * @return the average rate
     */
    public final float reportRate() {
        long sum = 0;
        long duration = numOfSlides * slideSizeInMils;
        updateHistorgrams();
        for (long e : _histogram) {
            sum += e;
        }

        return sum / (float) duration;
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
    private void updateHistorgrams() {
        long windowsDiff, retainedWindows;

        windowsDiff = getCurrentWindowId() - _latestSlideId;

        if (windowsDiff == 0)
            return;

        retainedWindows = Math.max(0, numOfSlides - windowsDiff);

        for (int i = 0; i < numOfSlides; i++) {
            if (i < retainedWindows)
                _histogram[i] = _histogram[i + (int) windowsDiff];
            else
                _histogram[i] = 0;
        }

        _latestSlideId = getCurrentWindowId();

    }

    private long getSlideId(long ticks) {
        return ticks / slideSizeInMils;
    }
}
