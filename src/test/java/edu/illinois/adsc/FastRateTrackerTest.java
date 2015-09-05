package edu.illinois.adsc;

/**
 * Created by Robert on 9/5/15.
 */
public class FastRateTrackerTest {

    /**
     * Test if the tracker could eclipse all the slides
     */
    public void testEclipsedAllWindows() {
        FastRateTracker rt = new FastRateTracker(10000, 10, true);
        rt.notify(10);
        rt.forceUpdateSlides(10);
        assert (rt.reportRate() == 0);
    }

    public void testEclipsedOneWindow() {
        FastRateTracker rt = new FastRateTracker(10000, 10, true);
        rt.notify(1);
        float r1 = rt.reportRate();
        rt.forceUpdateSlides(1);
        rt.notify(1);
        float r2 = rt.reportRate();

        System.out.format("r1:%f, r2:%f\n",r1, r2);

        assert (r1 == r2);
    }

    public void testEclipsedNineWindows() {
        FastRateTracker rt = new FastRateTracker(10000, 10, true);
        rt.notify(1);
        float r1 = rt.reportRate();
        rt.forceUpdateSlides(9);
        rt.notify(9);
        float r2 = rt.reportRate();

        assert (r1 == r2);
    }
}
