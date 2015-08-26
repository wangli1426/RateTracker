package edu.illinois.adsc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for RateTracker
 */
public class RateTrackerTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RateTrackerTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(RateTrackerTest.class);
    }

    /**
     * Tests:
     */
    public void testApp() {
        assertTrue(true);
    }

    /**
     * Test if the tracker could eclipse all the slides
     */
    public void testEclipsedAllWindows() {
        RateTracker rt = new RateTracker(true);
        rt.notify(10);
        rt.set_ticks(rt.get_ticks() + RateTracker.numOfSlides * RateTracker.slideSizeInMils);

        assert (rt.reportRate() == 0);
    }

    public void testEclipsedOneWindow() {
        RateTracker rt = new RateTracker(true);
        rt.notify(1);
        float r1 = rt.reportRate();
        rt.set_ticks(rt.get_ticks() + RateTracker.slideSizeInMils);
        rt.notify(1);
        float r2 = rt.reportRate();

        assert (2 * r1 == r2);
    }

    public void testEclipsedTwoWindow() {
        RateTracker rt = new RateTracker(true);
        rt.notify(1);
        float r1 = rt.reportRate();
        rt.set_ticks(rt.get_ticks() + 2 * RateTracker.slideSizeInMils);
        rt.notify(1);
        float r2 = rt.reportRate();

        assert (2 * r1 == r2);
    }

}
