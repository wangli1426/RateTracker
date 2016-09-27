package edu.illinois.adsc;


import junit.framework.TestCase;

/**
 * Unit test for RateTracker
 */
public class RateTrackerTest extends TestCase {


    /**
     * Test if the tracker could eclipse all the slides
     */
    public void testEclipsedAllWindows() {
        RateTracker rt = new RateTracker(10000, 10, true);
        rt.notify(10);
        rt.set_ticks(rt.get_ticks() + rt.get_numOfSlides() * rt.get_slideSizeInMils());

        assert (rt.reportRate() == 0);
    }

    public void testEclipsedOneWindow() {
        RateTracker rt = new RateTracker(10000, 10, true);
        rt.notify(1);
        float r1 = rt.reportRate();
        rt.set_ticks(rt.get_ticks() + rt.get_slideSizeInMils());
        rt.notify(1);
        float r2 = rt.reportRate();

        assert (2 * r1 == r2);
    }

    public void testEclipsedNineWindows() {
        RateTracker rt = new RateTracker(10000, 10, true);
        rt.notify(1);
        float r1 = rt.reportRate();
        rt.set_ticks(rt.get_ticks() + 9 * rt.get_slideSizeInMils());
        rt.notify(1);
        float r2 = rt.reportRate();

        assert (2 * r1 == r2);
    }

}
